package coolraw.test;

import org.springframework.stereotype.Controller;

import coolraw.annotation.RequestMapping;
import coolraw.view.ModelAndView;
import coolraw.view.SimpleModelAndView;




@Controller
@RequestMapping(value="/aa/hh/dd")
public class TestController2 {
	
	@RequestMapping(value="/asas")
	public String testrrr(String a){
		System.out.println(a+"成功！！！！");
		return "redirect:test";
	}
	
	@RequestMapping(value="/person/{age}")
	public ModelAndView testsse(String a,Integer age){
		System.out.println(a+"成功！！！！"+age);
		ModelAndView modelAndView=new SimpleModelAndView();
		modelAndView.setViewPath("forward:test");
		modelAndView.addObject("bb", 66);
		return modelAndView;
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
