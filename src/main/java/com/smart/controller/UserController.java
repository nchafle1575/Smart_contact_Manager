  package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME" + userName);

		// get the username email

		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER" + user);
		model.addAttribute("user", user);
	}

	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		return "normal/user_dashboard";
	}

	// open form add handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model, Principal principal) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	// process add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file.....
			if (file.isEmpty()) {
				System.out.println("file is empty!!!!!!!!!!!");
//				contact.setImage("contact.png");

			} else {
				System.out.println("inside else blok");
				contact.setImage(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("images is uploaded");
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);

			System.out.println("DATA" + contact.toString());
			System.out.println("Added to database");

			// message success...
			session.setAttribute("message", new Message("Your COntact is added", "success"));

		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.getStackTrace();

			// error message
			session.setAttribute("message", new Message("Something went Wrong!! try again", "danger"));
		}
		return "normal/add_contact_form";
	}

	// show contact handler
	//per page 5
	//current page
	
	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page") Integer page ,Model m, Principal principal) {
		m.addAttribute("title", "Show User Contact");

		// contact ki list ko bhejna hy
		
		Pageable pageable = PageRequest.of(page, 5);
	
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		Page<Contact> contacts =  this.contactRepository.findContactByUser(user.getId(), pageable);
			
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contact";
	}
	
	//showing particular contact details
	@RequestMapping("/{cid}/contact")
	public String showContactDetails(@PathVariable("cid") Integer cid, Model m, Principal principal) {
		System.out.println("CID"+ cid);
		
		
		Optional<Contact> contactOpt= this.contactRepository.findById(cid);
		Contact contacts = contactOpt.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId() == contacts.getUser().getId())
			m.addAttribute("contacts", contacts);
		
		
		return "normal/contact_details";
	}
	
	@GetMapping("delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model m, HttpSession session) {
		
		
		Optional<Contact> contactOpational = this.contactRepository.findById(cid);
		Contact contact = contactOpational.get();
		
		contact.setUser(null);
		this.contactRepository.delete(contact);
		
		session.setAttribute("message", new Message("Contact deleted succesfully...", "success"));
		return "redirect:/user/show-contact/0";
		
	}
	
}
