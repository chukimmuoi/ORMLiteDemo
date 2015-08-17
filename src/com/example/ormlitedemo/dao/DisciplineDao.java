package com.example.ormlitedemo.dao;

import java.sql.SQLException;

import com.example.ormlitedemo.cdp.Discipline;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class DisciplineDao extends BaseDaoImpl<Discipline, Integer>{

	public DisciplineDao(ConnectionSource cs) throws SQLException {
		super(Discipline.class);
		setConnectionSource(cs);
		initialize();
	}

}
