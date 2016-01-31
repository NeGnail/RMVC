package coolraw.test;

import org.springframework.stereotype.Controller;

import coolraw.annotation.RequestMapping;
import coolraw.view.ModelAndView;
import coolraw.view.SimpleModelAndView;




@Controller
@RequestMapping(value="/test/tt/ee")
public class TestController {
	
	@RequestMapping(value="/String")
	public ModelAndView test(String a){
		System.out.println(a+"成功！！！！");
		ModelAndView modelAndView=new SimpleModelAndView();
		modelAndView.setViewPath("forward:test");
		modelAndView.addObject("aa", 11);
		return modelAndView;
	}
	
	@RequestMapping(value="/person/{age}")
	public String tests(String a,Integer age){
		System.out.println(a+"成功！！！！"+age);
		return "redirect:test";
	}
	
//	@RequestMapping(value="/Float")
//	public ModelAndView testst(Double a){
////		System.out.println(a+"成功！！！！");
////		ModelAndView modelAndView=new ModelAndView();
////		modelAndView.addObject("ss", "fdfd");
////		modelAndView.setPath("test");
//		
//		return modelAndView;
//		
//	}
	
	
	@RequestMapping(value="/wei")
	public void testssss(Integer a){
		System.out.println(a+"成功！！！！真开心");
	}
}
