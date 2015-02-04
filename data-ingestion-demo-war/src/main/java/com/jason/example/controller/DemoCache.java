package com.jason.example.controller;

import java.util.List;

import com.jason.example.model.ObjectInfo;

public class DemoCache {
	private static List<ObjectInfo> infoList;

	public static List<ObjectInfo> getInfoList() {
		return infoList;
	}

	public static void setInfoList(List<ObjectInfo> infoList) {
		DemoCache.infoList = infoList;
	}
}
