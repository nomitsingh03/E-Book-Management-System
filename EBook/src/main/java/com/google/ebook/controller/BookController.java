package com.google.ebook.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.dependency.AbstractBeansOfTypeDependsOnDatabaseInitializationDetector;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.ebook.entity.BookDetails;
import com.google.ebook.entity.Cart;
import com.google.ebook.entity.User;
import com.google.ebook.helper.CartHelper;
import com.google.ebook.helper.Message;
import com.google.ebook.repository.BookRepository;
import com.google.ebook.repository.UserRepository;
import com.google.ebook.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CartHelper cartHelper;
	
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@Autowired
	private UserRepository userRepo;
	
	private User user;
	
	@Autowired
	private BookRepository bookRepo;
	
	private int no_of_items=0;
	
	@Value("${project.image}")
	private String path;
	
	@ModelAttribute("user")
	public void display(Model model, Principal p) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
		DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
		String username = user.getAttribute("email");
		
		model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
		this.user=userRepo.findByEmail(username);
		model.addAttribute("user",user);
		}else {
			if(p!=null) {
			this.user = userRepo.findByEmail(p.getName());
			model.addAttribute("user", user);
			}
		}
		model.addAttribute("cartQuantity", cartHelper.common(user));
		System.out.println(p);
		 
	}
	
	

	@PostMapping("/save_book")
	public String addBook(@ModelAttribute("book") BookDetails book,@RequestParam("bookImage") MultipartFile file, HttpSession session) throws IOException {
		if(!file.isEmpty()) {
			book.setBookImageName(bookService.uploadImage(path, file, book.getBookName()));
		}   book.setUser(user);
			book.setUserEmail(user.getEmail());
			bookRepo.save(book);
			System.out.println(book);
			session.setAttribute("msg",new Message("Book Added Successfully!!", "alert-success","fa-circle-check"));
		
		return "redirect:/admin/addBook";
	}
	
	@GetMapping("/all_BookDetails")
	public String viewAllBooks(Model m) {
		if(user==null) return "redirect:/login";
		List<BookDetails>books  =bookRepo.findAll();
		m.addAttribute("books", books);
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/all_books";
	}
	
	@GetMapping("/all_New_BookDetails")
	public String viewAllNewBooks(Model m) {
		if(user==null) return "redirect:/login";
		List<BookDetails>books  =bookService.getAllNewBooks();
		m.addAttribute("books", books);
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/all_books";
	}
	
	@PostMapping("/update_book")
	public String updateBook(@ModelAttribute("book") BookDetails book,@RequestParam("bookImage") MultipartFile file, HttpSession session) throws IOException {
		BookDetails old = bookRepo.findById(book.getBook_id()).get();
		if(file.isEmpty()) {
			book.setBookImageName(old.getBookImageName());
		} else {
			bookService.deleteImage(path, book);
			book.setBookImageName(bookService.uploadImage(path, file, book.getBookName()));
		}
			book.setUser(user);
			book.setUserEmail(user.getEmail());
			bookRepo.save(book);
			System.out.println("12345");
			session.setAttribute("msg",new Message("Book Updated Successfully!!", "alert-success","fa-circle-check"));
			
		return "redirect:/admin/addBook";
	}
	
	@GetMapping("/update-book-details/{id}")
	public String update(@PathVariable("id") int id, Model m) {
		m.addAttribute("book",bookRepo.findById(id).get());
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "admin/update_book";
	}
	
	@GetMapping("/view_book/{id}")
	public String viewBook(@PathVariable("id") int id,Model m) {
		if(user==null) return "redirect:/login";
		BookDetails book = bookRepo.findById(id).get();
		m.addAttribute("book", book);
		m.addAttribute("seller", book.getUser());
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "normal/book_view";
	}
	
	@GetMapping("/view_book/{id}/description")
	public String viewBookDetails(@PathVariable("id") int id,Model m) {
		if(user==null) return "redirect:/login";
		BookDetails book = bookRepo.findById(id).get();
		m.addAttribute("book", book);
		m.addAttribute("seller", book.getUser());
		m.addAttribute("user", user);
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "normal/bookDetails";
	}
	
	@GetMapping("/user/all_recentBooks")
	public String viewRecent(Model m) {
		if(user==null) return "redirect:/login";
		else {
			List<BookDetails> bookList = bookService.getAllRecentBooks();
			m.addAttribute("books",bookList);
			m.addAttribute("user", user);
			m.addAttribute("cartQuantity", cartHelper.common(user));
			return "normal/all_recent_book";
		}
	}
	
	@GetMapping("/user/all_newBooks")
	public String viewNew(Model m) {
		if(user==null) return "redirect:/login";
		else {
			List<BookDetails> bookList = bookService.getAllNewBooks();
			m.addAttribute("books",bookList);
			m.addAttribute("user", user);
			m.addAttribute("cartQuantity", cartHelper.common(user));
			return "normal/all_new_book";
		}
	}
	
	@GetMapping("/user/all_oldBooks")
	public String viewOld(Model m) {
		System.out.println(user);
		if(user==null) return "redirect:/login";
		else {
			List<BookDetails> bookList = bookService.getAllOldBooks();
			m.addAttribute("books",bookList);
			m.addAttribute("user", user);
			m.addAttribute("cartQuantity", cartHelper.common(user));
			return "normal/all_new_book";
		}
	}
	
	@GetMapping("/user/books/communication")
	public String communicationBook(Model m) {
		m.addAttribute("user", user);
		List<BookDetails> books = bookRepo.findByBookCategory("communication");
		m.addAttribute("books",books );
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "normal/comn";
	}
	
	@GetMapping("/user/books/programming")
	public String programmingBook(Model m) {
		m.addAttribute("user", user);
		List<BookDetails> books = bookRepo.findByBookCategory("programming");
		m.addAttribute("books",books );
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "normal/programming";
	}
	
	@GetMapping("/user/books/computer-science")
	public String ComputerBook(Model m) {
		m.addAttribute("user", user);
		List<BookDetails> books = bookRepo.findByBookCategory("ComputerFundamentals");
		m.addAttribute("books",books );
		m.addAttribute("cartQuantity", cartHelper.common(user));
		return "normal/computer";
	}
	
}
