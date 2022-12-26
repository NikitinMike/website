package com.example.website;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.example.website.Utils.notNull;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "words", schema = "words")
public class WordsBookEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "IID")
    private long iid;
    @Basic
    @Column(name = "word")
    private String word;
    @Basic
    @Column(name = "code")
    private long code;
    @Basic
    @Column(name = "code_parent")
    private long codeParent;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "type_sub")
    private String typeSub;
    @Basic
    @Column(name = "type_ssub")
    private String typeSsub;
    @Basic
    @Column(name = "plural")
    private Byte plural;
    @Basic
    @Column(name = "gender")
    private String gender;
    @Basic
    @Column(name = "wcase")
    private String wcase;
    @Basic
    @Column(name = "comp")
    private String comp;
    @Basic
    @Column(name = "soul")
    private Byte soul;
    @Basic
    @Column(name = "transit")
    private String transit;
    @Basic
    @Column(name = "perfect")
    private Byte perfect;
    @Basic
    @Column(name = "face")
    private String face;
    @Basic
    @Column(name = "kind")
    private String kind;
    @Basic
    @Column(name = "time")
    private String time;
    @Basic
    @Column(name = "inf")
    private Byte inf;
    @Basic
    @Column(name = "vozv")
    private Byte vozv;
    @Basic
    @Column(name = "nakl")
    private String nakl;

    public WordsBookEntity(String word) {
        this.word = word;
//        this.iid = (long) (5000000+Math.random()*1000000);
//        this.type= "сущ";
//        this.gender = "ср";
//        this.wcase = "им";
    }

    @Override
    public String toString() {
        return "{" + word + ":" +
                Utils.wordSplit(word)
//                + "iid=" + iid
//                + ", word='" + word + '\''
                + notNull("", code)
                + notNull("parent", codeParent)
                + notNull("", type)
                + notNull("typeSub", typeSub)
                + notNull("typeSsub", typeSsub)
                + notNull("plural", plural)
                + notNull("gender", gender)
                + notNull("wcase", wcase)
                + notNull("comp", comp)
                + notNull("soul", soul)
                + notNull("transit", transit)
                + notNull("perfect", perfect)
                + notNull("face", face)
                + notNull("kind", kind)
                + notNull("time", time)
                + notNull("inf", inf)
                + notNull("vozv", vozv)
                + notNull("nakl", nakl)
                + "}";
    }
}