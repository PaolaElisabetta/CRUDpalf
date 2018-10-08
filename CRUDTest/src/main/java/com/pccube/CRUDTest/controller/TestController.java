package com.pccube.CRUDTest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pccube.CRUDTest.entities.Task;
import com.pccube.CRUDTest.entities.TaskRepository;
import com.pccube.CRUDTest.entities.User;
import com.pccube.CRUDTest.entities.UserRepository;

@Controller
public class TestController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskRepository taskRepository;

	@RequestMapping("/user")
	public String login(Model model) {

		model.addAttribute("users", userRepository.findAll());

		return "user";
	}

	@RequestMapping(value = "/task", produces = "application/json")
	@ResponseBody
	public List<Task> read(Model model) {

		List<Task> tasks = (List<Task>) taskRepository.findAll();

		return tasks;
	}
	
	@RequestMapping(value = "/taskForUser", produces = "application/json")
	@ResponseBody
	public List<Task> readForUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User u = userRepository.findByUsername(username);
		List<Task> tasks = new ArrayList<Task>();//(List<Task>) taskRepository.findByUser(u);
		
		List<Task> tasksAll = (List<Task>) taskRepository.findAll();
		for(Task t:tasksAll) {
			if(t.getUser_id() == u)
				tasks.add(t);
		}

		return tasks;
	}
	
	@RequestMapping(value = "/taskTest", method = RequestMethod.POST)
	public String task(Model model, @RequestParam("description") String description, @RequestParam("type") String type,
			@RequestParam("father") String father, @RequestParam("user") String user) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));

		User u = userRepository.findByUsername(user);

		Task task = new Task();
		task.setDescrizione(description);
		task.setType(type);
		task.setUser_id(u);

		User usr = userRepository.findByUsername(user);

		// Se inserisco padre e nel form padre metto qualcosa
		// sto sbagliando perchè non posso assegnare padre a padre!

		if (type.equals("father") && !father.equals("")) {

			model.addAttribute("error", "Non è possibile assegnare task padre ad un padre");
			return "home";
		}

		// Qui ci sono un po' di passaggi in piu', ma se non li metto
		// misteriosamente crasha tutto GRRR
		// Cmq sostanzialmente controllo tramite l'id inserito se quel task esiste
		Task taskf = null;
		if (!father.equals("")) {
			Long id = Long.parseLong(father);
			List<Task> tasks = (List<Task>) taskRepository.findAll();

			for (Task t : tasks) {
				if (t.getId() == id) {
					taskf = t;
				}
			}
		}

		// Se quel task non esiste lancio un errore!!!
		if (taskf == null && !father.equals("")) {
			model.addAttribute("error", "Inserisci Task Valido");
			return "home";
		} else if (task != null && !father.equals("")) {

			taskf.getChildren().add(task);

		}

		// Se non sono Admin non posso assegnare
		if (!role && !user.equals("")) {
			model.addAttribute("error", "Puoi assegnare un task solo se sei admin");
			return "home";
		}
		if (usr == null && !user.equals("")) {
			model.addAttribute("error", "Username non valido");
			return "home";
		}
		if (usr != null) {
			usr.getTasks().add(task);
		}
		taskRepository.save(task);
		model.addAttribute("message", "Task aggiunto con successo");
		return "home";
	}

}
