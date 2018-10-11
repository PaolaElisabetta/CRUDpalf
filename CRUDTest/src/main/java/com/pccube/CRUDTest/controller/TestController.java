package com.pccube.CRUDTest.controller;

import java.util.ArrayList;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	// Caricare utenti presenti nel db nella pagina listUser
	@RequestMapping("/user")
	public String login(Model model) {

		model.addAttribute("users", userRepository.findAll());

		return "user";
	}

	// Reindirizzare in base al ruolo la pagina home da listUser
	@RequestMapping("/back")
	public String back(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean role = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("admin"));

		if (role) {
			model.addAttribute("users", userRepository.findAll());
			return "homeAdmin";
		}

		return "homeUser";

	}

	// Ritornare in formato JSON la lista di tutti i task
	@RequestMapping(value = "/task", produces = "application/json")
	@ResponseBody
	public List<Task> read(Model model) {

		List<Task> tasks = (List<Task>) taskRepository.findAll();

		return tasks;
	}

	// Ritorna tutti i task dell'utente loggato
	@RequestMapping(value = "/taskForUser", produces = "application/json")
	@ResponseBody
	public List<Task> readForUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User u = userRepository.findByUsername(username);
		List<Task> tasks = new ArrayList<Task>();// (List<Task>) taskRepository.findByUser(u);

		List<Task> tasksAll = (List<Task>) taskRepository.findAll();
		for (Task t : tasksAll) {
			if (t.getUser_id() == u)
				tasks.add(t);
		}

		return tasks;
	}

	// Carica nella <select> gli utenti presenti nel db
	@GetMapping("/homeAdmin")
	public String showUsers(Model model, Task tsk) {

		model.addAttribute("users", userRepository.findAll());
		// model.addAttribute("task", taskRepository.findAll());
		return "homeAdmin";
	}


	// La logica che permette la creazione e il salvataggio di un nuovo TASK
	// con i relativi controlli.

	@RequestMapping(value = "/taskTest", method = RequestMethod.POST) public
	 String task(@Valid Task task,BindingResult bindingResult,Model model, @RequestParam("type") String type,
	  	  @RequestParam("father") String father, @RequestParam("user") String user) {
	  
	  
	  model.addAttribute("users", userRepository.findAll()); //
	 // model.addAttribute("task", taskRepository.findAll());
	  
	  if (bindingResult.hasErrors()) {
			return "homeAdmin";
		}
	  
	  
	  
	  Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
	  boolean role =  authentication.getAuthorities().stream().anyMatch(r ->  r.getAuthority().equals("admin"));
	  
	  
	  User u = userRepository.findByUsername(user); 
	  Task tsk = new Task();
	  
	  tsk.setDescription(task.getDescription());
	  tsk.setType(type); 
	  tsk.setUser_id(u);
	  
	  User usr = userRepository.findByUsername(user);
	  
	  // Se inserisco padre e nel form padre metto qualcosa // sto sbagliando
	//  perchè non posso assegnare padre a padre!
	  
	  if (type.equals("father") && !father.equals("")) {
	  
	  model.addAttribute("error", "Non è possibile assegnare task padre ad un padre"); return "homeAdmin"; }
	  
	  Task taskf = null;
	  if (!father.equals("")) { 
		  Long id =  Long.parseLong(father); List<Task> tasks = (List<Task>)
	  taskRepository.findAll();
	  
	  for (Task tt : tasks) { if (tt.getId() == id) { taskf = tt; } } }
	  
	  // Se quel task non esiste lancio un errore!!!
	  if (taskf == null &&
	  !father.equals("")) { model.addAttribute("error", "Inserisci Task Valido");
	  return "homeAdmin"; } else if (task != null && !father.equals("")) {
	  
	  taskf.getChildren().add(task);
	  
	  }
	  
	  // Se non sono Admin non posso assegnare
	  if (!role && !user.equals("")) {
	  model.addAttribute("error", "Puoi assegnare un task solo se sei admin");
	  return "homeAdmin"; }

	if(usr==null&&!user.equals(""))

	{
		model.addAttribute("error", "Username non valido");
		return "homeAdmin";
	}if(usr!=null)
	{
		usr.getTasks().add(tsk);
	} 
	
	taskRepository.save(tsk);
	model.addAttribute("message", "Task aggiunto con successo");
	
	return"homeAdmin";
}

}
