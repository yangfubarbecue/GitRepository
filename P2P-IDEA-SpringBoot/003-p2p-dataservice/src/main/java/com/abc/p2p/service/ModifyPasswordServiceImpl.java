package com.abc.p2p.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service(interfaceClass = ModifyPasswordService.class, timeout = 20000, version = "1.0.0")
@Component
public class ModifyPasswordServiceImpl implements ModifyPasswordService {
}
