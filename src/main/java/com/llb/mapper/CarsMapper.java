package com.llb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.llb.entity.Cars;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 车辆信息表 Mapper 接口
 * </p>
 *
 * @author llb
 * @since 2020-04-30
 */
public interface CarsMapper extends BaseMapper<Cars> {

    IPage<Map<String, Object>> carList(IPage<Map<String, Object>> pageParam, @Param("account") String account);

    void deleteCar(String carId);

    void editCar(Cars cars);

    void saveCar(Cars car);

}
