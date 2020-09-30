package com.llb.controller;


import com.alibaba.fastjson.JSONObject;
import com.llb.entity.Cars;
import com.llb.entity.Teacher;
import com.llb.service.ICarsService;
import com.llb.service.ITeacherService;
import com.llb.service.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 车辆信息表 前端控制器
 * </p>
 *
 * @author llb
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/cars")
public class CarsController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private ICarsService carsService;


}

