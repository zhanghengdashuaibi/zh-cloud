package com.csbr.cloud.tcc.server;

import org.hibernate.service.spi.ServiceException;

import java.util.HashMap;

/**
 * @author zhangheng
 * @date 2019/11/12 15:50
 * TCC基础服务  Confirm  Cancel都需要实现此接口
 */
public interface IbaseTccService {

    public void increase(HashMap map) throws ServiceException;

    public void decrease(HashMap map) throws ServiceException;
}
