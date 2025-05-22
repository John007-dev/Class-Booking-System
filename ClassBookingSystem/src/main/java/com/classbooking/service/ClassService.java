package com.classbooking.service;

// import java.sql.SQLException; // <<< REMOVED UNUSED IMPORT
import java.time.LocalDateTime;
import java.util.List;

import com.classbooking.dao.ClassInfoDAO;
import com.classbooking.dao.ClassScheduleDAO;
import com.classbooking.model.ClassInfo;
import com.classbooking.model.ClassSchedule;

public class ClassService {
    private ClassInfoDAO classInfoDAO;
    private ClassScheduleDAO classScheduleDAO;

    public ClassService() {
        this.classInfoDAO = new ClassInfoDAO();
        this.classScheduleDAO = new ClassScheduleDAO();
    }

    public List<ClassSchedule> getAvailableClassSchedulesWithDetails() {
        return classScheduleDAO.getAllAvailableSchedulesWithClassTitle();
    }

    public ClassSchedule getScheduleDetailsById(int scheduleId) {
        ClassSchedule schedule = classScheduleDAO.getScheduleById(scheduleId);
        if (schedule != null) {
            if (schedule.getClassInfo() == null && schedule.getClassId() > 0) {
                ClassInfo classInfo = classInfoDAO.getClassById(schedule.getClassId());
                schedule.setClassInfo(classInfo);
            }
        }
        return schedule;
    }

    public boolean addClass(String title, String description, String instructorName, double durationHours, double price, String imageUrl) {
        ClassInfo newClass = new ClassInfo(title, description, instructorName, durationHours, price, imageUrl);
        return classInfoDAO.addClass(newClass);
    }

    public List<ClassInfo> getAllClassInfos() {
        return classInfoDAO.getAllClasses();
    }
    
    public ClassInfo getClassInfoById(int classId) {
        return classInfoDAO.getClassById(classId);
    }

    public boolean updateClassInfo(ClassInfo classInfo) {
        return classInfoDAO.updateClass(classInfo);
    }

    public boolean deleteClassInfo(int classId) {
        return classInfoDAO.deleteClass(classId);
    }

    public boolean addScheduleForClass(int classId, LocalDateTime startTime, LocalDateTime endTime, String location, int capacity) {
        if (classInfoDAO.getClassById(classId) == null) {
            System.err.println("Cannot add schedule: Class with ID " + classId + " not found.");
            return false;
        }
        if (startTime == null || endTime == null || startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
                System.err.println("Invalid start/end times for schedule.");
                return false;
        }
        if (capacity <=0) {
            System.err.println("Capacity must be positive.");
            return false;
        }

        ClassSchedule newSchedule = new ClassSchedule(classId, startTime, endTime, location, capacity);
        return classScheduleDAO.addSchedule(newSchedule);
    }
    
    public List<ClassSchedule> getSchedulesForClass(int classId) {
        return classScheduleDAO.getSchedulesByClassId(classId);
    }

    public boolean updateClassSchedule(ClassSchedule schedule) {
        return classScheduleDAO.updateSchedule(schedule);
    }

    public boolean deleteClassSchedule(int scheduleId) {
        return classScheduleDAO.deleteSchedule(scheduleId);
    }
}
