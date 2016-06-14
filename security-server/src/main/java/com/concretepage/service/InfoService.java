package com.concretepage.service;

import org.springframework.stereotype.Service;

@Service
public class InfoService implements IInfoService {
	public String getMsg() {
		return "Hello ";
	}
}
