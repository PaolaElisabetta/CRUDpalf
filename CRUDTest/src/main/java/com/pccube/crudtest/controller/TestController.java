package com.pccube.crudtest.controller;

import java.util.ArrayList;


import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pccube.crudtest.entities.Task;
import com.pccube.crudtest.entities.TaskDTO;
import com.pccube.crudtest.entities.TaskRepository;
import com.pccube.crudtest.entities.User;
import com.pccube.crudtest.entities.UserRepository;

@Controller
public class TestController {

	private static final String PAGE = "homeAdmin";
	private static final String ERROR = "error";
	private static final String USERS = "users";

	@Autowired
	UserRepository userRepository;

	@Autowired
	TaskRepository taskRepository;

	// Caricare utenti presenti nel db nella pagina listUser
	@RequestMapping("/user")
	public String login(Model model) {

		model.addAttribute(USERS, userRepository.findAll());

		return "user";
	}

	// Reindirizzare in base al ruolo la pagina home da listUser
	@RequestMapping("/back")
	public String back(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));

		if (role) {
			model.addAttribute(USERS, userRepository.findAll());
			return PAGE;
		}

		return "homeUser";

	}

	// Ritornare in formato JSON la lista di tutti i task
	@RequestMapping(value = "/task", produces = "application/json")
	@ResponseBody
	public List<Task> read(Model model) {
		
		return (List<Task>) taskRepository.findAll();
	}

	// Ritorna tutti i task dell'utente loggato
	@RequestMapping(value = "/taskForUser", produces = "application/json")
	@ResponseBody
	public List<Task> readForUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User u = userRepository.findByUsername(username);
		List<Task> tasks = new ArrayList<>();

		List<Task> tasksAll = (List<Task>) taskRepository.findAll();
		for (Task t : tasksAll) {
			if (t.getUserId() == u)
				tasks.add(t);
		}

		return tasks;
	}

	// Carica nella <select> gli utenti presenti nel db
	@GetMapping("/homeAdmin")
	public String showUsers(Model model) {

		model.addAttribute(USERS, userRepository.findAll());
		return PAGE;
	}

	// La logica che permette la creazione e il salvataggio di un nuovo TASK
	// con i relativi controlli.

	@PostMapping(value = "/taskTest")
	public String task(@Valid TaskDTO task, BindingResult bindingResult, Model model, @RequestParam("type") String type,
			@RequestParam("father") String father, @RequestParam("user") String user) {

		model.addAttribute(USERS, userRepository.findAll()); 

		if (bindingResult.hasErrors()) {
			return PAGE;
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));

		User u = userRepository.findByUsername(user);
		Task tsk = new Task();

		tsk.setDescription(task.getDescription());
		tsk.setType(type);
		tsk.setUserId(u);

		User usr = userRepository.findByUsername(user);

		// Se inserisco padre e nel form padre metto qualcosa // sto sbagliando
		// perchè non posso assegnare padre a padre!

		if (type.equals("father") && !father.equals("")) {

			model.addAttribute(ERROR, "Non è possibile assegnare task padre ad un padre");
			return PAGE;
		}

		if (!father.equals("")) {
			Long id = Long.parseLong(father);
			Optional<Task> taskf = taskRepository.findById(id);

			if (taskf.isPresent()) {
				taskf.get().getChildren().add(tsk);
			} else {
				model.addAttribute(ERROR, "Inserisci Task Valido");
				return PAGE;
			}
		}
		// Se non sono Admin non posso assegnare
		if (!role && !user.equals("")) {
			model.addAttribute(ERROR, "Puoi assegnare un task solo se sei admin");
			return PAGE;
		}

		if (usr == null && !user.equals("")) {
			model.addAttribute(ERROR, "Username non valido");
			return PAGE;
		}
		if (usr != null) {
			usr.getTasks().add(tsk);
		}

		taskRepository.save(tsk);
		model.addAttribute("message", "Task aggiunto con successo");

		return PAGE;
	}

}
