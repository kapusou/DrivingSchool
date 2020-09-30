package com.llb.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Cars;
import com.llb.mapper.CarsMapper;
import com.llb.service.ICarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 车辆信息表 服务实现类
 * </p>
 *
 * @author llb
 * @since 2020-04-30
 */
@Service
public class CarsServiceImpl extends ServiceImpl<CarsMapper, Cars> implements ICarsService {

    @Autowired
    private CarsMapper carsMapper;

    /**
     * 分页查询车辆信息列表
     * @return
     */
    @Override
    public IPage<Map<String, Object>> carList(IPage<Map<String, Object>> pageParam, String account) {
        return carsMapper.carList(pageParam, account);
    }

    /**
     * 删除车辆信息
     * @param carId
     */
    @Override
    public void deleteCar(String carId) {
        carsMapper.deleteCar(carId);
    }

    /**
     * 修改车辆信息
     * @param cars
     */
    @Override
    public void editCar(Cars cars) {
        carsMapper.editCar(cars);
    }

    /**
     * 保存车辆信息
     * @param car
     */
    @Override
    public void saveCar(Cars car) {
        carsMapper.saveCar(car);
    }
}
