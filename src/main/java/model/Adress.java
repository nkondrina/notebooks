package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Поль on 07.01.2016.
 */
public class Adress {
    public  Adress(){
    }

    public Adress(Person person, String adress) {
        this.person = person;
        this.adress = adress;
    }

    @ManyToOne
    public Person getPerson() {
        return person;
    }

    @Column(name = "adress")
    public String getAdress() {
        return adress;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    private Long id;
    private Person person;
    private String adress;

}
