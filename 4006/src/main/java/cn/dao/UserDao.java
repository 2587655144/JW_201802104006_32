package cn.dao;

import cn.domain.User;
import cn.service.TeacherService;
import cn.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}

	/**
	 * 修改密码
	 * @param user 用户
	 * @param newPassword 新密码
	 * @throws SQLException
	 */
	public void changePassword(User user, String newPassword) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "update user set password = ? where id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,newPassword);
		pstmt.setInt(2,user.getId());
		//执行预编译语句
		pstmt.executeUpdate();
		//关闭资源
		JdbcHelper.close(pstmt,connection);
	}

	/**
	 * 登陆
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @throws SQLException
	 */
	public User login(String username,String password) throws SQLException {
		User user = null;
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String login_sql = "select * from user where username = ? and password = ?";
		//在该连接上创建预编译语句
		PreparedStatement pstmt = connection.prepareStatement(login_sql);
		//为参数赋值
		pstmt.setString(1,username);
		pstmt.setString(2,password);
		//执行预编译语句
		ResultSet resultSet = pstmt.executeQuery();
		if (resultSet.next()){
			user = UserService.getInstance().find(resultSet.getInt("id"));

		}
		return user;
	}


	public Collection<User> findAll() throws SQLException {
		Collection<User> users = new TreeSet<User>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement stmt = connection.createStatement();
		//执行SQL语句
		ResultSet resultSet = stmt.executeQuery("select * from user");
		while(resultSet.next()){
			User user = new User(
					resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);

			users.add(user);
		}
		return users;
	}

	/**
	 * 按id查询
	 * @param id
	 * @return 返回用户
	 * @throws SQLException
	 */
	public User find(Integer id) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "select * from user where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		User user = new User(
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("loginTime"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		return user;
	}

	public User findByUsername(String username) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "select * from user where username = ?";
		PreparedStatement pstmt = connection.prepareStatement(findUser_sql);
		pstmt.setString(1,username);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		User user = new User(
				resultSet.getInt("id"),
				resultSet.getString("username"),
				resultSet.getString("password"),
				resultSet.getDate("password"),
				TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
		);
		return user;
	}


	public void update(User user) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "update user set username = ?,password = ?,loginTime = ?,teacher_id = ? where id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setString(1,user.getUsername());
		pstmt.setString(2,user.getPassword());
		//pstmt中getDate()与setDate(datesql)中返回与传入的参数都是java.sql.date,所以要先进行转换
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		pstmt.setInt(5,user.getId());
		pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
	}

	public void add(User user) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String addUser_sql = "insert into user(username,password,loginTime,teacher_id) values" +
				" (?,?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		//pstmt中getDate()与setDate(datesql)中返回与传入的参数都是java.sql.date,所以要先进行转换
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
        //关闭资源
		JdbcHelper.close(pstmt,connection);
	}

	public void add(Connection connection,User user) throws SQLException {
		String addUser_sql = "insert into user(username,password,loginTime,teacher_id) values" +
				" (?,?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(addUser_sql);
		pstmt.setString(1, user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3, (Date) user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了 " + affectedRowNum +" 行记录");
		pstmt.close();
	}

	public void delete(Integer id) throws SQLException {
		User user = this.find(id);
		this.delete(user);
	}

	public void delete(User user) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String deleteUser_sql = "delete from user where id = ?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
		pstmt.setInt(1,user.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 " + affectedRowNum +" 行记录");
        //关闭资源
		JdbcHelper.close(pstmt,connection);
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}

}
