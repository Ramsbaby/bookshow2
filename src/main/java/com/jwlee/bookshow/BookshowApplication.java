package com.jwlee.bookshow;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jwlee.bookshow.webapp.common.ApiService;
import com.jwlee.bookshow.webapp.common.SessionManager;

/**
* DemoApplication
* @author jungwoolee
* @since 2019-07-22
**/
@Controller
@EnableJpaAuditing
@SpringBootApplication
//@RibbonClient(name = "hi-service", configuration = ServiceLoadbalancerConfiguration.class)
public class BookshowApplication{
//	private final RestTemplate restTemplate;
//	private final LoadBalancerClient loadBalancer;

	@Autowired
	ApiService apiService;

//	public BookshowApplication(RestTemplate restTemplate, LoadBalancerClient loadBalancer) {
//		this.restTemplate = restTemplate;
//		this.loadBalancer = loadBalancer;
//	}

	public static void main(String[] args) {
		SpringApplication.run(BookshowApplication.class, args);
	}

//	@Bean
//	public ApplicationListener<ApplicationReadyEvent> listener1() {
//		return it -> {
//			String url = UriComponentsBuilder.fromHttpUrl("http://hi-service/")
//					.build()
//					.toUriString();
//			String response = restTemplate.getForObject(url, String.class);
//			System.out.println(response);
//		};
//	}

//	@Bean
//	public ApplicationListener<ApplicationReadyEvent> listener2() {
//		return it -> {
//			ServiceInstance instance = loadBalancer.choose("hi-service");
//			System.out.println(format("%s:%d", instance.getHost(), instance.getPort()));
//		};
//	}



	//********************   로그인 페이지 관련   ********************
	@RequestMapping(value = {"/","/login","/login.do"})
	String loginJsp(HttpSession session) throws Exception{
		session.invalidate();
		return "login/login";
	}

	@RequestMapping(value="/login/popup/pAccountAdd.do") public String pAccountAdd(Model model) {return "login/popup/pAccountAdd";}
	//********************   로그인 페이지 관련   ********************



	//********************   메인 페이지 관련   ********************
	@RequestMapping("/main/bookSearch.do")
	String bookSearch() throws Exception{
		// check session
		if(SessionManager.getUser() == null) {
			return "redirect:/login.do";
		}
		return "main/bookSearch";
	}
	@RequestMapping("/main/myHistory.do")
	String myHistory() throws Exception{
		if(SessionManager.getUser() == null) {
			return "redirect:/login.do";
		}
		return "main/myHistory";
	}
	@RequestMapping("/main/bestSeller.do")
	String bestSeller() throws Exception{
		if(SessionManager.getUser() == null) {
			return "redirect:/login.do";
		}
		return "main/bestSeller";
	}
	//********************   메인 페이지 관련   ********************
}
