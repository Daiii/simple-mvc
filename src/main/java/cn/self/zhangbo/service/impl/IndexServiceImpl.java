package cn.self.zhangbo.service.impl;

import cn.self.zhangbo.kernel.annotation.Service;
import cn.self.zhangbo.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService
{
    
    @Override
    public String sayHello(String name)
    {
        return name;
    }
}
