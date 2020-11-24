package com.whut.controller;

import com.whut.dao.UserRepository;
import com.whut.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
@Controller
public class EmployeeController {

    @Autowired
    UserRepository userRepository;
//    @Autowired
//    EmployeeDao employeeDao;
//
//    @Autowired
//    DepartmentDao departmentDao;
//查询所有员工返回列表页面

    //来到员工添加页面
    @GetMapping("/emp")
    public String toAddPage(Model model){
        //来到添加页面,查出所有的部门，在页面显示
//        Collection<Department> departments = departmentDao.getDepartments();
//        model.addAttribute("depts",departments);
        return "emp/add";
    }
//
//    //员工添加
//    //SpringMVC自动将请求参数和入参对象的属性进行一一绑定；要求请求参数的名字和javaBean入参的对象里面的属性名是一样的
    @PostMapping("/emp")
    public String addEmp(User user){
        //来到员工列表页面

        System.out.println("保存的员工信息："+user);
        //保存员工
        userRepository.save(user);
        // redirect: 表示重定向到一个地址  /代表当前项目路径
        // forward: 表示转发到一个地址
        return "redirect:/emps";
    }
//
    //来到修改页面，查出当前员工，在页面回显
    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id,Model model){
        User user = userRepository.getOne(id);
        model.addAttribute("user",user);

        //页面要显示所有的部门列表
//        Collection<Department> departments = departmentDao.getDepartments();
//        model.addAttribute("depts",departments);
        //回到修改页面(add是一个修改添加二合一的页面);
        return "emp/update";
    }
//
    //员工修改；需要提交员工id；
    @PostMapping("/empUpdate")
    public String updateEmployee(User user){
        System.out.println("修改的员工数据："+user);
        userRepository.save(user);
        return "redirect:/emps";
    }



    //员工删除
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id){
    //    employeeDao.delete(id);
        userRepository.delete(id);
        return "redirect:/emps";

}


    @GetMapping("/emps")
    public String  list(Model model){
//        Collection<Employee> employees = employeeDao.getAll();
        List<User> users = userRepository.findAll();

        //放在请求域中
        model.addAttribute("users",users);
        // thymeleaf默认就会拼串
        // classpath:/templates/xxxx.html
        System.out.println("收到emps请求");
        return "emp/list";
    }
}
