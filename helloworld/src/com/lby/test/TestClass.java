package com.lby.test;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.lby.solarAndLunarCalender.DateSolarLunar;
import com.lby.solarAndLunarCalender.Lunar;
import com.lby.solarAndLunarCalender.Solar;

public class TestClass {

	@Test
	public void test1() {
		Solar date2Solar = DateSolarLunar.date2Solar(new Date());
		System.out.println("今日阳历：" + DateSolarLunar.dump(date2Solar));
		Lunar date2Lunar = DateSolarLunar.date2Lunar(new Date());
		System.out.println("今日农历：" + DateSolarLunar.dump(date2Lunar));
	}

}
