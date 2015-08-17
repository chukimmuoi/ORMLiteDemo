package com.example.ormlitedemo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.ormlitedemo.cdp.Discipline;
import com.example.ormlitedemo.cdp.Student;
import com.example.ormlitedemo.dao.DatabaseHelper;
import com.example.ormlitedemo.dao.DisciplineDao;
import com.example.ormlitedemo.dao.StudentDao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;


public class MainActivity extends ActionBarActivity {
	private DatabaseHelper dh;
	private StudentDao studentDao;
	private DisciplineDao disciplineDao;
	private List<Student> students;
	private Student s;
	private int firstId = 0; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dh = new DatabaseHelper(MainActivity.this);
        students = new ArrayList<Student>();
        s = new Student();
        s.setName("CHỬ KIM MƯỜI");
        s.setDisciplines(Arrays.asList(new Discipline("Toán", "TOAN"), new Discipline("SỬ", "SU")));
        students.add(s);
        
        s = new Student();
        s.setName("VŨ THÀNH LĂNG");
        s.setDisciplines(Arrays.asList(new Discipline("ANH", "ANH"), new Discipline("VĂN", "VA")));
        students.add(s);
        
        try {
			studentDao = new StudentDao(dh.getConnectionSource());
//			studentDao.delete(studentDao.queryForAll());
			disciplineDao = new DisciplineDao(dh.getConnectionSource());
			
			//CREATE
			for (Student student : students) {
				int result = studentDao.create(student);
				if(result == 1){
					for (Discipline discipline : student.getDisciplines()) {
						discipline.setStudent(student);
						disciplineDao.create(discipline);
					}
					firstId = firstId == 0 ? student.getId() : firstId;
				}
			}
			
			//GET ALL LINES
			Log.i("Script", " ");
			Log.i("Script", "GET ALL LINES");
			students = studentDao.queryForAll();
			for (Student student : students) {
				Log.i("Script", "Name: " + student.getName() + "\nID: "+ student.getId() +"\nDisciplines: " + student.getDisciplines().size());
				for (Discipline discipline : student.getDisciplines()) {
					Log.i("Script", "          Discipline: " + discipline.getName() + "\n          ID: "+ discipline.getId() +"\n          Code: " + discipline.getCode());
					Log.i("Script", " ");
				}
				Discipline discipline = new Discipline("ĐỊA", "DI");
				discipline.setStudent(student);
				disciplineDao.create(discipline);
				
				student.setName(student.getName() + "- ANDROID CLASS");
				studentDao.update(student);
			}
			
			//GET ALL LINES AGAIN
			Log.i("Script", " ");
			Log.i("Script", "GET ALL LINES AGAIN");
			students = studentDao.queryForAll();
			for (Student student : students) {
				Log.i("Script", "Name: " + student.getName() + "\nID: "+ student.getId() +"\nDisciplines: " + student.getDisciplines().size());
				for (Discipline discipline : student.getDisciplines()) {
					Log.i("Script", "          Discipline: " + discipline.getName() + "\n          ID: "+ discipline.getId() +"\n          Code: " + discipline.getCode());
					Log.i("Script", " ");
				}
			}
			
			//GET SPECIFIC LINE BY ID
			Log.i("Script", " ");
			Log.i("Script", "GET SPECIFIC LINE BY ID");
			s = studentDao.queryForId(firstId);
			Log.i("Script", "Name: " + s.getName() + "\nID: "+ s.getId() +"\nDisciplines: " + s.getDisciplines().size());
			for (Discipline discipline : s.getDisciplines()) {
				Log.i("Script", "          Discipline: " + discipline.getName() + "\n          ID: "+ discipline.getId() +"\n          Code: " + discipline.getCode());
				Log.i("Script", " ");
			}
			disciplineDao.delete(s.getDisciplines());
			
			//GET SPECIFIC LINE BY NAME
			Log.i("Script", " ");
			Log.i("Script", "GET SPECIFIC LINE BY NAME");
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("name", "VŨ THÀNH LĂNG- ANDROID CLASS");
			students = studentDao.queryForFieldValues(values);
			for (Student student : students) {
				Log.i("Script", "Name: " + student.getName() + "\nID: "+ student.getId() +"\nDisciplines: " + student.getDisciplines().size());
				for (Discipline discipline : student.getDisciplines()) {
					Log.i("Script", "          Discipline: " + discipline.getName() + "\n          ID: "+ discipline.getId() +"\n          Code: " + discipline.getCode());
					Log.i("Script", " ");
					disciplineDao.delete(discipline);
				}
			}
			
//			//GET ALL LINES AGAIN
//			Log.i("Script", " ");
//			Log.i("Script", "GET ALL LINES AGAIN");
//			students = studentDao.queryForAll();
//			for (Student student : students) {
//				Log.i("Script", "Name: " + student.getName() + "\nID: "+ student.getId() +"\nDisciplines: " + student.getDisciplines().size());
//				for (Discipline discipline : student.getDisciplines()) {
//					Log.i("Script", "Discipline: " + discipline.getName() + "\nID: "+ discipline.getId() +"\nCode: " + discipline.getCode());
//				}
//			}
			
			//GET ALL LINES AGAIN BY ROW
			Log.i("Script", " ");
			Log.i("Script", "GET ALL LINES AGAIN BY ROW");
			GenericRawResults<Student> raw = studentDao.queryRaw("SELECT id, name FROM student WHERE name LIKE \"CHỬ%\"", new RawRowMapper<Student>() {
				@Override
				public Student mapRow(String[] columnNam, String[] results)
						throws SQLException {
					return new Student(Integer.parseInt(results[0]), results[1]);
				}
			});
			students = studentDao.queryForAll();
			for (Student student : raw) {
				Log.i("Script", "Name: " + student.getName() + "\nID: "+ student.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dh.close();
	}
}
