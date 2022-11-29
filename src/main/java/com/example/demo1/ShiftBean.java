package com.example.demo1;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named(value = "ShiftBean")
@RequestScoped
public class ShiftBean implements Serializable {
    public static class Shift{
        private int id;
        private String date;
        private String beginTime;
        private String endTime;
        private StaffBean.Employee employee;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public StaffBean.Employee getEmployee() {
            return employee;
        }

        public void setEmployee(StaffBean.Employee employee) {
            this.employee = employee;
        }
    }


}
