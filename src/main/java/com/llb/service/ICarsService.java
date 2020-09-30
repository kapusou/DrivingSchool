package com.llb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Cars;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 车辆信息表 服务类
 * </p>
 *
 * @author llb
 * @since 2020-04-30
 */
public interface ICarsService extends IService<Cars> {


    /**
     * 分页查询车辆信息列表
     * @return
     */
    IPage<Map<String, Object>> carList(IPage<Map<String, Object>> pageParam, String account);

    /**
     * 删除车辆信息
     * @param carId
     */
    void deleteCar(String carId);

    /**
     * 修改车辆信息
     * @param cars
     */
    void editCar(Cars cars);

    /**
     * 保存车辆信息
     * @param car
     */
    void saveCar(Cars car);

}
