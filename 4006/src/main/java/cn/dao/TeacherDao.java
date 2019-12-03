package cn.dao;

import cn.domain.Teacher;
import cn.domain.User;
import cn.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			Teacher teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
			teachers.add(teacher);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		return teachers;
	}

	public Teacher find(Integer id) throws SQLException{
		//声明一个Teacher类型的变量
		Teacher teacher = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "select * from teacher where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}

	public boolean update(Teacher teacher) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateTeacher_sql = " update teacher set no = ?,name=?,profTitle_id=?,degree_id=?,department_id=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,teacher.getNo());
		preparedStatement.setString(2,teacher.getName());
		preparedStatement.setInt(3,teacher.getProfTitle().getId());
		preparedStatement.setInt(4,teacher.getDegree().getId());
		preparedStatement.setInt(5,teacher.getDepartment().getId());
		preparedStatement.setInt(6,teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	
	public void add(Teacher teacher) throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addTeacher_sql = "insert into teacher(no,name,profTitle_id,department_id,degree_id) values" +
				" (?,?,?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addTeacher_sql);
		//为预编译参数赋值
		pstmt.setString(1, teacher.getNo());
		pstmt.setString(2, teacher.getName());
		pstmt.setInt(3, teacher.getProfTitle().getId());
		pstmt.setInt(4, teacher.getDepartment().getId());
		pstmt.setInt(5, teacher.getDegree().getId());
		//执行预编译语句
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
        //获得主键
		ResultSet resultSet = pstmt.getGeneratedKeys();
		resultSet.next();
		//得到新增记录的id
		int teacher_id = resultSet.getInt(1);
		//为本方法的teacher赋id
		teacher.setId(teacher_id);
		Date date_util = new Date();
		Long date_long = date_util.getTime();
		java.sql.Date date_sql = new java.sql.Date(date_long);
		//创建user对象，关联已经拥有id的Teacher对象
		User user = new User(teacher.getName(),teacher.getNo(),date_sql,teacher);
		//然后将User对象保存到表中
		UserService.getInstance().add(connection,user);
		//关闭资源
		JdbcHelper.close(pstmt, connection);
	}

	public boolean delete(int id) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "delete from teacher where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		Teacher teacher1 = TeacherDao.getInstance().find(1);
		System.out.println(teacher1);
		teacher1.setName("李四");
		TeacherDao.getInstance().update(teacher1);
		Teacher teacher2 = TeacherDao.getInstance().find(1);
		System.out.println(teacher2.getName());
	}

}
