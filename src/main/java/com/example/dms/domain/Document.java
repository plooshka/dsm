package com.example.dms.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Document implements Comparable<Document> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Не может быть пустым")
    @Length(min = 5, max = 50, message = "Не может быть менее 5 или более 50 символов")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String VUZ;

    private Double finish;

    public Document(){
    }

    public Document(String text, User author, String VUZ, Double finish) {
        this.author = author;
        this.text = text;
        this.VUZ = VUZ;
        this.finish = finish;
    }


    public void setVUZ(String VUZ) {
        this.VUZ = VUZ;
    }

    public String getVUZ() {
        return VUZ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Double getFinish() {
        return finish;
    }

    public void setFinish(Double finish) {
        this.finish = finish;
    }

    @Override
    public int compareTo(Document obj)
    {
        Document tmp = (Document) obj;
        if(this.finish < tmp.finish)
        {
            /* текущее меньше полученного */
            return -1;
        }
        else if(this.finish > tmp.finish)
        {
            /* текущее больше полученного */
            return 1;
        }
        /* текущее равно полученному */
        return 0;
    }

}
