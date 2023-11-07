package org.example;

import org.example.entity.TeacherWatch;

import java.util.ArrayList;
import java.util.Objects;

public class TeachingUnit {
    private String groups;
    private String teacher;
    private String discipline;

    @Override
    public String toString() {
        return  teacher + ' '+ discipline + groups;
    }

    public TeachingUnit(String teacher ,String discipline, String groups) {
        this.groups = groups;
        this.teacher = teacher;
        this.discipline = discipline;
    }

    public TeachingUnit() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachingUnit that = (TeachingUnit) o;
        return Objects.equals(groups, that.groups) && Objects.equals(teacher, that.teacher) && Objects.equals(discipline, that.discipline);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, teacher, discipline);
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}


