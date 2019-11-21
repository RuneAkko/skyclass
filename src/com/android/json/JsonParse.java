package com.android.json;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.sql.DocService;
import com.android.sql.DtcourseService;
import com.android.sql.DtplaycourseService;
import com.android.sql.DtstructureService;
import com.android.domain.*;

public class JsonParse {
	
	

	DocService courseDocService;
	DtplaycourseService dtplaycourseService;
	//DtstructureService  dtstructureService;
	private Context context;
	DtcourseService  dtcourseService;
	private ArrayList<Course> myList = new ArrayList();
	private String studentCode;
	int screenWidth=0;
	private ObjectMapper objectMapper = new ObjectMapper();
	private ArrayList<NewsFile> myNewsList=new ArrayList();
	private ArrayList<NewsDataFile> myDataList=new ArrayList();
	private Course  cb=new Course();
	public ArrayList<Course> getCourse() {
		return myList;
	}

	public void setCourse(ArrayList<Course> myList) {
		this.myList = myList;
	}

	//得到相关的新闻列表
	public ArrayList<NewsFile> getMyNewsList() {
			return myNewsList;
		}
	//设置相关的新闻列表
	public void setMyNewsList(ArrayList<NewsFile> myNewsList) {
		this.myNewsList = myNewsList;
	}

	//得到新闻中的相关的内容
	public ArrayList<NewsDataFile> getMyDataList() {
		return myDataList;
	}

	public void setMyDataList(ArrayList<NewsDataFile> myDataList) {
		this.myDataList = myDataList;
	}

	public JsonParse(Context context) {
		this.context=context;
		dtcourseService=new DtcourseService(context);
		courseDocService = new DocService(context);
		dtplaycourseService=new DtplaycourseService(context);
		//dtstructureService=new DtstructureService(context);
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE); 
		screenWidth = wm.getDefaultDisplay().getWidth(); 

	}
	//登录成功后对获取的数据信息进行解析
	public void JSONToBean(String json) {
		try {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
			Map<String, Object> map1 = list.get(0);
			Set<String> set1 = map1.keySet();
		    //获得当前登录的学生的code
			for (Iterator<String> itt = set1.iterator(); itt.hasNext();) {
				String key = itt.next();
				System.out.println(key + ":" + map1.get(key));
				if (key.compareTo("StudentCode") == 0)
					studentCode=map1.get(key).toString();                      
			}
			for (int i = 1; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				Set<String> set = map.keySet();
				Course temp = new Course();
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String key = it.next();
					System.out.println(key + ":" + map.get(key));
					if(key.compareTo("CourseID")==0)
						temp.setCourseId(map.get(key).toString());
					if (key.compareTo("CourseCode") == 0){
						temp.setCourseCode(map.get(key).toString());
						System.out.println(map.get(key).toString()+"courseCodecourseCode");}
					if (key.compareTo("CourseName") == 0)
						temp.setCourseName(map.get(key).toString());

				}
				temp.setStudentCode(studentCode);
				myList.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//解析获得的相关的文档的信息，并保存在数据库中
	public void JsonCourseDoc(String json,String id){
		try{
			Gson gson = new Gson();
			TableData tableDatadoc = gson.fromJson(json,  
	                new TypeToken<TableData>() {  
	                }.getType());  
			List tableDoclist = tableDatadoc.getRows();
			String strtmp = gson.toJson(tableDoclist);
			List<courseDoc> doclist = gson.fromJson(strtmp, new TypeToken<List<courseDoc>>(){
			                          }.getType());
			 for (int j = 0; j < doclist.size(); j++) {  
                 System.out.println(doclist.get(j).getArticleTypeName()); 
                 courseDocService.save(doclist.get(j),id);
             } 
			
		}
		catch(Exception e){e.printStackTrace();}	
	}

	//针对只有第一层目录的情况
	public void JsonCourseS(String json,String cno){
		dtcourseService.save(json,cno);
	}

	//针对没有二级目录结构的情况
	public void JsonCourse(String json,String cno){
		try {

			Gson gson=new Gson();
			List<TableData> tableDatas2 = gson.fromJson(json, new TypeToken<List<TableData>>() { }.getType());
			System.out.println("tableDatas"+tableDatas2.size());
			for (int i = 0; i < tableDatas2.size(); i++) {
				TableData entityData = tableDatas2.get(i);
				String tableName = entityData.getName();
				List tableData = entityData.getRows();
				String s2 = gson.toJson(tableData);
				List<Dtcourse> dtcourseList = gson.fromJson(s2, new TypeToken<List<Dtcourse>>(){}.getType());
				List<Dtplaycourse> dtplaycourseList = gson.fromJson(s2, new TypeToken<List<Dtplaycourse>>(){}.getType());

				//获取课程的描述信息
				if (tableName.equals("dtcourse")) {
					System.out.println("dtcourse");

					for (int j = 0; j < dtcourseList.size(); j++) {
						System.out.println(dtcourseList.get(j).getname());
						dtcourseService.save(dtcourseList.get(j));
					}
				}
				//获取其中的每节课的信息
				else  if (tableName.equals("dtplaycourse")) {
					System.out.println("dtplaycourse");

					for (int j = 0; j < dtplaycourseList.size(); j++) {
						dtplaycourseList.get(j).setsvpath(dtplaycourseList.get(j).getsvpath_abs());//直接将svpath_abs赋值给svpath
						dtplaycourseService.save(dtplaycourseList.get(j),cno);
					}
				}

			}

		}
		catch(Exception e){
		    e.printStackTrace();
		}
	}

	//解析课程信息并保存在数据库中
	public void JsonCourse(String json){
			try {
				
			      Gson gson=new Gson();
			      List<TableData> tableDatas2 = gson.fromJson(json, new TypeToken<List<TableData>>() { }.getType());
				  System.out.println("tableDatas"+tableDatas2.size());
			        for (int i = 0; i < tableDatas2.size(); i++) {  
			            TableData entityData = tableDatas2.get(i);
			            String tableName = entityData.getName();  
			            List tableData = entityData.getRows();  
			            String s2 = gson.toJson(tableData);  
			            List<Dtcourse> dtcourseList = gson.fromJson(s2, new TypeToken<List<Dtcourse>>(){}.getType());
			            List<Dtplaycourse> dtplaycourseList = gson.fromJson(s2, new TypeToken<List<Dtplaycourse>>(){}.getType());
			            //List<Dtstructure> dtstructureList = gson.fromJson(s2, new TypeToken<List<Dtstructure>>(){}.getType());
			            //获取课程的描述信息
			            if (tableName.equals("dtcourse")) {  
			                System.out.println("dtcourse");  
			               
			                for (int j = 0; j < dtcourseList.size(); j++) {  
			                    System.out.println(dtcourseList.get(j).getname()); 
			                    dtcourseService.save(dtcourseList.get(j));
			                }  
			            }
			            //获取其中的每节课的信息
			            else  if (tableName.equals("dtplaycourse")) {  
			                System.out.println("dtplaycourse");  
			               
			                for (int j = 0; j < dtplaycourseList.size(); j++) {  
			                	dtplaycourseService.save(dtplaycourseList.get(j));
			                 
			                } 		  
			            }   
			            //获取其中的章节信息
			           /* else  if (tableName.equals("dtstructure")) {
			                System.out.println("dtstructure");  
			               
			                for (int j = 0; j < dtstructureList.size(); j++) {  
			                	dtstructureService.save(dtstructureList.get(j));
			                  
			                }  
			  
			            }*/
			        }  
			 
		    	}
			catch(Exception e){e.printStackTrace();}			
		
			
		
		
	 
		
	}
	//锟斤拷锟斤拷锟叫憋拷Json锟斤拷萁锟斤拷锟�
		public void NewsJSONToBean(String  json) {
				
		        try {
		        	            
		            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);

		            System.out.println(list.size());
		            for (int i = 0; i < list.size(); i++) {
		                Map<String, Object> map = list.get(i);
		                Set<String> set = map.keySet();
		                NewsFile temp=new NewsFile();
		                for (Iterator<String> it = set.iterator();it.hasNext();) {
		                    String key = it.next();
		                    System.out.println(key + ":" + map.get(key));
		                    if(key.compareTo("id")==0)  temp.setId(map.get(key).toString());
		                    if(key.compareTo("title")==0)  temp.setTitle(map.get(key).toString());
		                    if(key.compareTo("keywords")==0)  temp.setKeywords(map.get(key).toString());
		                    if(key.compareTo("description")==0)  temp.setDescription(map.get(key).toString());	                    
		                }
		                myNewsList.add(temp);
		            }
		        } catch (Exception e) {	
		            e.printStackTrace();
		        }
		    }

		
		//锟斤拷锟斤拷锟斤拷锟斤拷Json锟斤拷萁锟斤拷锟�
		public NewsDataFile DataJSONToBean(String  json) {
			 NewsDataFile temp=new NewsDataFile();
	        try {
	   	
//	        	json=json.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "").replaceAll("</[a-zA-Z]+[1-9]?>", "").replaceAll("&nbsp;", "").replaceAll("&nbsp","").replaceAll("&ldquo;", "").replaceAll("&rdquo;", "").replaceAll("\r","      ").replaceAll("\n","").replaceAll("\t","").replaceAll("\f","").replaceAll("\b","");
	        	json=json.replace('\n',' ').replace('\r',' ').replace('\t',' ').replace('\b',' ').replace('\f',' ').replaceAll("&nbsp;", "").replaceAll("&nbsp","").replaceAll("&ldquo;", "").replaceAll("&rdquo;", "");
	        	json="[" + json + "]";
	        	
	        
	            @SuppressWarnings("unchecked")
				List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);

	            System.out.println(list.size());
	         //   for (int i = 0; i < list.size(); i++) {
	                Map<String, Object> map = list.get(0);
	                Set<String> set = map.keySet();
	               
	                for (Iterator<String> it = set.iterator();it.hasNext();) {
	                    String key = it.next();
	                    System.out.println(key + ":" + map.get(key));
	                    String content=map.get(key).toString();
	                  //  String regEx="<.+?>"; //锟斤拷示锟斤拷签 
	                  String start="<div style=\"word-wrap: break-word; word-break: normal; width : +screenWidth+;\">";
	                  String end="</div>";
	                  String result=start.concat(content).concat(end);
	                /*    Pattern p=Pattern.compile(regEx); 
	                    Matcher m=p.matcher(content); 
	                    String s=m.replaceAll(""); */
	                 
	                    if(key.compareTo("content")==0)  temp.setContent(result);  
	                    System.out.println("wwwwwwwwwwwwwwwww"+screenWidth);
	                    System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeee"+result);
	                }
	                
	        //    }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return temp;
	    }	
}
