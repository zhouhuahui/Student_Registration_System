package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	String id;
	String password;
	String name;
	String birthday;
	String SSN;
	String status;
	String graduation_date;
	Schdule schdule;

	Connection conn;
	PreparedStatement pst;
	ResultSet rs;

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;

	public Student(Socket socket) {
		id = null;

		password = null;
		name = null;
		birthday = null;
		SSN = null;
		status = null;
		graduation_date = null;
		this.socket = socket;
		try {
			this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));// 输出流
			this.conn = Database.getNewConnection();
			this.pst = null;
			this.rs = null;
		} catch (IOException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public String login(String id, String pw) throws SQLException {

		this.id = id;
		this.password = pw;
		/*
		 * 当服务器接收到学生客户端发来的登录请求后，创建学生对象并执行此方法 1.访问数据库，对id与pw进行检索，若检索不到或不匹配，报错，返回0
		 * 2.若匹配，则检索数据库该学生的信息，存放到本对象中，并返回1表示登录成功。
		 */
		// 补充：对数据库进行检索
		String sql;
		sql = "select sid,password from student where sid=?";
		pst = conn.prepareStatement(sql);
		pst.setString(1, id);
		rs = pst.executeQuery();
		if(!rs.next()) {
			return "3";//用户名不正确
		}
		String testidString=rs.getString("sid");
		String testpwString=rs.getString("password");
		
		if(!pw.contentEquals(testpwString)) {
			return "2";//密码不正确
		}
		else if(id.equals(testidString)&&pw.equals(testpwString)) {
			return "1";
		}
		return "0";
	}
	
	void createSchedule(String id, DataOutputStream dos) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			String sql;
			sql = "select * from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			if(rs.next()) {
				dos.writeChars("1");
				dos.flush();
			}else {
				ArrayList<String> cofs = new ArrayList<String>();
				sql = "select * from course";
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				
				Connection oldConn = Database.getOldConnection();
				PreparedStatement oldPst = null;
				ResultSet oldRs = null;
				while(rs.next()) {
					String strName = rs.getString("name");
					String strPre = rs.getString("prerequisite");
					if(strPre != null) { //从旧数据库中查询前导课对应的课程名字
						String sql2 = "select name from course where id=?";
						oldPst = oldConn.prepareStatement(sql2);
						oldPst.setString(1, strPre);
						oldRs = oldPst.executeQuery();
						oldRs.next();
						strPre = oldRs.getString("name");
					}
					String strTime = rs.getString("timeslot");
					String strNum = rs.getString("number");
					cofs.add(strName+"  前导:"+(strPre==null?"null":strPre)+"  时间:"+strTime+"  人数:"+strNum);
				}
				if(oldRs != null) {
					oldRs.close();
				}
				if(oldPst != null) {
					oldPst.close();
				}
				oldConn.close();
				
				dos.writeChars("2");
				dos.writeInt(cofs.size());
				for(int i=0;i<cofs.size();i++) {
					dos.writeUTF(cofs.get(i));
				}
				dos.flush();
			}
		}catch(Exception e) {
			try {
				dos.writeChars("3");
				dos.flush();
			}catch(Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	void updateSchedule(String id, DataOutputStream dos) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			String sql;
			sql = "select * from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			if(rs.next()) {
				dos.writeChars("1");
				for(int i=0;i<6;i++) {
					String lesson;
					lesson = rs.getString("lesson"+Integer.toString(i+1));
					if(rs.wasNull()) {
						dos.writeUTF("");
					}else {
						sql = "select name from course where cid=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, lesson);
						ResultSet rs2 = pst.executeQuery();
						rs2.next();
						lesson = rs2.getString("name");
						dos.writeUTF(lesson);
					}
				}
				dos.writeUTF(rs.getString("status"));
				ArrayList<String> cofs = new ArrayList<String>();
				sql = "select * from course";
				pst = conn.prepareStatement(sql);
				rs = pst.executeQuery();
				
				Connection oldConn = Database.getOldConnection();
				PreparedStatement oldPst = null;
				ResultSet oldRs = null;
				while(rs.next()) {
					String strName = rs.getString("name");
					String strPre = rs.getString("prerequisite");
					if(strPre != null) {
						String sql2 = "select name from course where id=?";
						oldPst = oldConn.prepareStatement(sql2);
						oldPst.setString(1, strPre);
						oldRs = oldPst.executeQuery();
						oldRs.next();
						strPre = oldRs.getString("name");
					}
					String strTime = rs.getString("timeslot");
					String strNum = rs.getString("number");
					cofs.add(strName+"  前导:"+(strPre==null?"null":strPre)+"  时间:"+strTime+"  人数:"+strNum);
				}
				if(oldRs != null) {
					oldRs.close();
				}
				if(oldPst != null) {
					oldPst.close();
				}
				oldConn.close();
				
				dos.writeInt(cofs.size());
				for(int i=0;i<cofs.size();i++) {
					dos.writeUTF(cofs.get(i));
				}
				dos.flush();
			}else {
				dos.writeChars("2");
				dos.flush();
			}
		}catch(Exception e) {
			try {
				dos.writeChars("3");
				dos.flush();
			}catch(Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	void deleteSchedule(String id, DataOutputStream dos) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			String sql;
			sql = "select * from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			if(rs.next() && rs.getString("status").equals("enrolled_in")) {
				dos.writeChars("1");
				for(int i=0;i<6;i++) {
					String lesson;
					lesson = rs.getString("lesson"+Integer.toString(i+1));
					if(rs.wasNull()) {
						dos.writeUTF("");
					}else {
						sql = "select name from course where cid=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, lesson);
						ResultSet rs2 = pst.executeQuery();
						rs2.next();
						lesson = rs2.getString("name");
						dos.writeUTF(lesson);
					}
				}
				dos.flush();
			}else {
				dos.writeChars("2");
				dos.flush();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	void submitDeleteSchedule(String id, DataOutputStream dos) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			String sql;
			//ResultSet rs;
			sql = "select * from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			rs.next();
			for(int i=1;i<=4;i++) {
				String cid = "lesson"+Integer.toString(i);
				cid = rs.getString(cid);
				if(rs.wasNull()) {
					continue;
				}
				sql = "update course set number=number-1 where cid=?"; //课程数量减少1
				pst = conn.prepareStatement(sql);
				pst.setString(1, cid);
				pst.executeUpdate();
			}
			sql = "delete  from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.executeUpdate();
			dos.writeUTF("success");
			dos.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	void saveSchedule(String id, DataOutputStream dos, ArrayList<String> lessons) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			//ResultSet rs;
			String sql;
			sql = "delete from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.executeUpdate();
			sql = "insert into course_selection values (?, ?, ?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			for(int i=0;i<6;i++) {
				String lesson = lessons.get(i);
				if(!lesson.equals("")) {
					String sql2 = "select cid from course where name=?";
					PreparedStatement pst2 = conn.prepareStatement(sql2);
					pst2.setString(1, lesson);
					rs = pst2.executeQuery();
					rs.next();
					String cid = rs.getString("cid");
					if(!cid.equals("")) {
						pst.setString(i+2, cid);
					}else {
						pst.setString(i+2, null);
					}
					pst2.close();
				}else {
					pst.setString(i+2, null);
				}
			}
			pst.setString(8, "saved");
			pst.executeUpdate();
			dos.writeUTF("success");
			dos.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * lessons中是选课的名字，
	 * 确定lessons中的选课的前导课都有学过或即将学
	 */
	void submitSchedule(String id, DataOutputStream dos, ArrayList<String> lessons) {
		try {
			//Connection conn = Database.getNewConnection();
			//System.out.println("实例化Statement对象");
			//PreparedStatement pst;
			String sql;
			//ResultSet rs;
			ArrayList<String> oldSubmittedMainLessonId = new ArrayList<String>(); //之前提交的主课,不含空字符串
			ArrayList<String> oldSubmittedAltLessonId = new ArrayList<String>(); //之前提交的备选课,不含空字符串
			sql = "select * from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,id);
			rs = pst.executeQuery();
			if(rs.next() && rs.getString("status").equals("enrolled_in")) {
				String lesson;
				lesson = rs.getString("lesson1");
				if(!rs.wasNull()) oldSubmittedMainLessonId.add(lesson);
				lesson = rs.getString("lesson2");
				if(!rs.wasNull()) oldSubmittedMainLessonId.add(lesson);
				lesson = rs.getString("lesson3");
				if(!rs.wasNull()) oldSubmittedMainLessonId.add(lesson);
				lesson = rs.getString("lesson4");
				if(!rs.wasNull()) oldSubmittedMainLessonId.add(lesson);
				lesson = rs.getString("lesson5");
				if(!rs.wasNull()) oldSubmittedAltLessonId.add(lesson);
				lesson = rs.getString("lesson6");
				if(!rs.wasNull()) oldSubmittedAltLessonId.add(lesson);
			}
			ArrayList<String> learntLessonId = new ArrayList<String>(); //以前学过的课程的id
			sql = "select cid from grade where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,id);
			rs = pst.executeQuery();
			while(rs.next()) {
				learntLessonId.add(rs.getString("cid"));
			}
			ArrayList<String> lessonId = new ArrayList<String>(); //当前正在提交的课程的ID
			for(int i=0;i<6;i++) {
				if(lessons.get(i).equals("")) {
					lessonId.add("");
					continue;
				}
				sql = "select cid from course where name=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1,lessons.get(i));
				rs = pst.executeQuery();
				rs.next();
				lessonId.add(rs.getString("cid"));
			}
			//检查前导课程是否满足
			for(int i=0;i<6;i++) {
				if(lessonId.get(i).equals("") 
						|| oldSubmittedMainLessonId.contains(lessonId.get(i)) 
						|| oldSubmittedAltLessonId.contains(lessonId.get(i))) 
					continue;
				//寻找前导课程
				sql = "select prerequisite from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, lessonId.get(i));
				rs = pst.executeQuery();
				if(rs.next() && rs.getString("prerequisite")!=null 
						&& !learntLessonId.contains(rs.getString("prerequisite"))) {
					//没有学过相应的前导课程
					Connection oldConn = Database.getOldConnection();
					PreparedStatement oldPst = null;
					ResultSet oldRs = null;
					sql = "select name from course where id=?";
					oldPst = oldConn.prepareStatement(sql);
					oldPst.setString(1, rs.getString("prerequisite"));
					oldRs = oldPst.executeQuery();
					oldRs.next();
					dos.writeChars("2");
					dos.writeUTF(lessons.get(i));
					dos.writeUTF(oldRs.getString("name"));
					dos.flush();
					if(oldRs != null) {
						oldRs.close();
					}
					if(oldPst != null) {
						oldPst.close();
					}
					oldConn.close();
					
					return;
				}
			}
			//检查每个课程是否时间上冲突
			/*int weekday[] = new int[8];
			String temp[] = new String[8];
			for(int i=0;i<8;i++) weekday[i] = 0;
			for(int i=0;i<6;i++) {
				if(lessonId.get(i).equals("")) continue;
				sql = "select timeslot from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, lessonId.get(i));
				rs = pst.executeQuery();
				rs.next();
				String timeslot = rs.getString("timeslot");
				if(!rs.wasNull()) {
					int date = 0;
					if(timeslot.equals("星期一")) {
						date = 1;
					}else if(timeslot.equals("星期二")) {
						date = 2;
					}else if(timeslot.equals("星期三")) {
						date = 3;
					}else if(timeslot.equals("星期四")) {
						date = 4;
					}else if(timeslot.equals("星期五")) {
						date = 5;
					}else if(timeslot.equals("星期六")) {
						date = 6;
					}else if(timeslot.equals("星期日")) {
						date = 7;
					}
					if(weekday[date] == 1) {
						//出现了时间冲突
						dos.writeChars("4");
						dos.writeUTF(temp[date]);
						dos.writeUTF(lessons.get(i));
						dos.flush();
						return;
					}else {
						weekday[date] = 1;
						temp[date] = lessons.get(i);
					}
				}
			}*/
			HashMap<String,String> map = new HashMap<String,String>();
			for(int i=0;i<6;i++) {
				if(lessonId.get(i).equals("")) continue;
				sql = "select timeslot from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, lessonId.get(i));
				rs = pst.executeQuery();
				rs.next();
				String timeslot = rs.getString("timeslot");
				if(!rs.wasNull()) {
					if(map.containsKey(timeslot)) {
						//出现了时间冲突
						dos.writeChars("4");
						dos.writeUTF(map.get(timeslot));
						dos.writeUTF(lessons.get(i));
						dos.flush();
						return;
					}else {
						map.put(timeslot, lessons.get(i));
					}
				}
			}
			
			//检查每个未提交的主课程是否满
			for(int i=0;i<4;i++) {
				if(lessonId.get(i).equals("") || oldSubmittedMainLessonId.contains(lessonId.get(i)))
					continue;
				sql = "select number from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, lessonId.get(i));
				rs = pst.executeQuery();
				rs.next();
				int num = rs.getInt("number");
				if(num >= 4) { //4是我设置的最大容量
					//选择的课程达到了最大的容量
					dos.writeChars("3");
					dos.writeUTF(lessons.get(i));
					dos.flush();
					return;
				}
			}
			//所有条件均满足，可以把课程加进去了
			for(int i=0;i<oldSubmittedMainLessonId.size();i++) {  //把以前提交的主课程删掉
				String cid = oldSubmittedMainLessonId.get(i);
				if(cid.equals("")) continue;
				sql = "select number from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, cid);
				rs = pst.executeQuery();
				rs.next();
				int num = rs.getInt("number");
				num--;
				sql = "update course set number=? where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, num);
				pst.setString(2, cid);
				pst.executeUpdate();
			}
			for(int i=0;i<4;i++) {
				String cid = lessonId.get(i);
				if(cid.equals("")) continue;
				sql = "select number from course where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, cid);
				rs = pst.executeQuery();
				rs.next();
				int num = rs.getInt("number");
				num++;
				sql = "update course set number=? where cid=?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, num);
				pst.setString(2, cid);
				pst.executeUpdate();
			}
			sql = "delete from course_selection where sid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.executeUpdate();
			sql = "insert into course_selection values (?, ?, ?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			for(int i=0;i<6;i++) {
				String cid = lessonId.get(i);
				if(!cid.equals("")) {
					pst.setString(i+2, cid);
				}else {
					pst.setString(i+2, null);
				}
			}
			pst.setString(8, "enrolled_in");
			pst.executeUpdate();
			dos.writeChars("1");
			dos.flush();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 退出学生注册
	 */
	void backStudRegistration() {
		SRSServer.isRegistration--;
		try {
			dos.writeUTF("success");
			dos.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ViewGrades() throws IOException {
		String semester = dis.readUTF();
		try {
			String sql;
			PreparedStatement pst;
			if (semester.equals("-----请选择-----")) {
				sql = "select semester,cid,cname,credit,grade " + "from grade " + "where sid = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, id);
			} else {
				sql = "select semester,cid,cname,credit,grade " + "from grade where semester = ? and " + "sid = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, semester);
				pst.setString(2, id);
			}
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				dos.writeUTF(rs.getString("semester"));
				dos.writeUTF(rs.getString("cid"));
				dos.writeUTF(rs.getString("cname"));
				dos.writeUTF(String.valueOf(rs.getString("credit")));
				if(rs.getString("grade")!=null)
					dos.writeUTF(rs.getString("grade"));
				else
					dos.writeUTF("");
				dos.flush();
			}
			dos.writeUTF("end");
			dos.flush();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			conn.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}

class Schdule {
	String stu_id;
	ArrayList<String> main_lesson;
	ArrayList<String> alternate_lesson;
	int status;// 当前课表的提交状态，0未提交，1已保存，2已提交

	public Schdule() {
		stu_id = null;
		main_lesson = new ArrayList<String>(4);
		;
		alternate_lesson = new ArrayList<String>(2);
		;
		status = 0;
	}

	public void edit_id(String id) {
		stu_id = id;
	}

	public void edit_status(int s) {
		status = s;
	}

	public void edit_main_lesson(int index, String lesson_id) {
		main_lesson.add(index, lesson_id);
	}

	public void edit_alternate_lesson(int index, String lesson_id) {
		alternate_lesson.add(index, lesson_id);
	}

}
