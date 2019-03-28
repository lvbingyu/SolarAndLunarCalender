package com.lby.solarAndLunarCalender;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DateSolarLunar {

	/**
	 * 将Date转换为Lunar
	 * 
	 * @param date
	 * @return
	 */
	public static Lunar date2Lunar(Date date) {
		return LunarSolarConverter.SolarToLunar(date2Solar(date));
	}

	/**
	 * 将Date转换为Solar
	 * 
	 * @param date
	 * @return
	 */
	public static Solar date2Solar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Solar solar = new Solar();
		solar.solarYear = year;
		solar.solarMonth = month;
		solar.solarDay = day;
		return solar;
	}

	/**
	 * Solar或者Lunar转为String
	 * 
	 * @param o
	 * @return
	 */
	public static String dump(Object o) {
		StringBuffer buffer = new StringBuffer();
		Class<?> oClass = o.getClass();
		if (oClass.isArray()) {
			buffer.append("[");
			for (int i = 0; i < Array.getLength(o); i++) {
				if (i > 0)
					buffer.append(",");
				Object value = Array.get(o, i);
				buffer.append(value.getClass().isArray() ? dump(value) : value);
			}
			buffer.append("]");
		} else {
			buffer.append("{");
			while (oClass != null) {
				Field[] fields = oClass.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if (buffer.length() > 1)
						buffer.append(",");
					fields[i].setAccessible(true);
					buffer.append(fields[i].getName());
					buffer.append("=");
					try {
						Object value = fields[i].get(o);
						if (value != null) {
							buffer.append(value.getClass().isArray() ? dump(value) : value);
						}
					} catch (IllegalAccessException e) {
					}
				}
				oClass = oClass.getSuperclass();
			}
			buffer.append("}");
		}
		return buffer.toString();
	}

	/**
	 * 获取指定年的阴阳历, 放入Map中，key为Lunar，value为Solar
	 * 
	 * @param year
	 * @return
	 */
	public static Map<Lunar, Solar> getEveryDayLunarSolar(int year) {
		Map<Lunar, Solar> everyLunarSolar = new HashMap();
		List<Date> everyDay = getEveryDay(year);
		for (Date date : everyDay) {
			everyLunarSolar.put(date2Lunar(date), date2Solar(date));
		}
		return everyLunarSolar;
	}

	/**
	 * 获取指定年的阴阳历, 放入Map中，key为String（Lunar:isleap,lunarMonth,lunarDay），value为Solar
	 * 
	 * @param year
	 * @return
	 */
	public static Map<String, Solar> getEveryDayLunarSolar2(int year) {
		Map<String, Solar> everyLunarSolar = new HashMap();
		List<Date> everyDay = getEveryDay(year);
		for (Date date : everyDay) {
			everyLunarSolar.put(date2Lunar(date).isleap + "" + date2Lunar(date).lunarMonth + "" + date2Lunar(date).lunarDay, date2Solar(date));
		}
		return everyLunarSolar;
	}

	/**
	 * 指定 阳历年份 阴历月份 阴历日，查阳历日期（年，月，日）
	 */
	public static Solar getSolarByLunar(int solar_year, int lunar_month, int lunar_day, boolean isleap) {
		Map<String, Solar> everyDayLunarSolar = getEveryDayLunarSolar2(solar_year);
		return everyDayLunarSolar.get(isleap + "" + lunar_month + "" + lunar_day);
	}

	/**
	 * 获取指定年份的每一天-Date
	 * 
	 * @param year
	 * @return
	 */
	public static List<Date> getEveryDay(int year) {
		List<Date> everyDay = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date firstDay = null;
		Date lastDay = null;
		try {
			firstDay = sdf.parse(year + "-01-01");
			lastDay = sdf.parse(year + "-12-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		everyDay.add(firstDay);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(firstDay);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(lastDay);
		while (c2.getTime().after(c1.getTime())) {
			c1.add(Calendar.DAY_OF_MONTH, 1);
			everyDay.add(c1.getTime());
		}
		return everyDay;
	}

	/**
	 * 传入年份 ，控制台输出该年阴阳历
	 */
	public static void printLunarAndSolar(int year) {
		Map<Lunar, Solar> lunarSolar = getEveryDayLunarSolar(year);
		Set<Map.Entry<Lunar, Solar>> entries = lunarSolar.entrySet();
		for (Map.Entry entry : entries) {
			System.out.println(dump(entry.getValue()) + "&&" + dump(entry.getKey()));
		}
	}

}
