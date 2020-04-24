/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.jax;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author user
 */

@XmlRootElement(name="root")

public class UserList {

    private List<User> userList= new ArrayList<>();;

    public UserList() {
        
    }

    public void add(User user) {
        userList.add(user);
    }

    @Override
    public String toString() {
        return "UserList{" + "userList=" + userList + '}';
    }

    
    @XmlElement(name = "user")
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
