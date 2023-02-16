package com.example.website;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.example.website.Utils.wordAnalyse;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "words", schema = "words")
public class WordBookEntity {
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
    public WordBookEntity(String word) {
        this.word = word;
//        this.iid = (long) (5000000+Math.random()*1000000);
        this.type = "";
//        this.type= "сущ";
//        this.gender = "ср";
//        this.wcase = "им";
    }

    public static String notNull(String tag, String s) {
        return s == null ? "" : String.format(",%s='%s'", tag, s);
    }

    public static String notNull(String tag, Long c) {
        return (c == null || 0 == c) ? "" : String.format(",%s=%d", tag, c);
    }

    public static String notNull(String tag, Byte b) {
        return (b == null || 0 == b) ? "" : String.format(",%s=%d", tag, b);
    }

    public String getType() {return type==null?"":type;}

// set('част','межд','прл','прч','сущ','нар','гл','дееп','союз','предик','предл','ввод','мест','числ')
// type_sub enum('поряд','кол','собир','неопр','врем','обст','опред','счет','неизм')
// type_ssub enum('кач','спос','степ','места','напр','врем','цель','причин')
// gender enum('муж','жен','ср','общ')
// wcase enum('им','род','дат','вин','тв','пр','зват','парт','мест')
//    comp enum('сравн','прев')
//    transit enum('перех','непер','пер/не')
//    face enum('1-е','2-е','3-е','безл')
//    kind enum('1вид','2вид')
//    time enum('прош','наст','буд')
//    nakl enum('пов','страд')

    public String getWord() {
        if (type != null)
            switch (type) {

                case "сущ": return "<i class=noun>" + wordAnalyse(word) + "</i>";

                case "прл": return "<i class=adj>" + wordAnalyse(word) + "</i>";
                case "числ": return "<b class=adj>" + wordAnalyse(word) + "</b>";

                case "гл": return "<i class=verb>" + wordAnalyse(word) + "</i>";
                case "нар": return "<b class=verb>" + wordAnalyse(word) + "</b>";
                case "ввод": return "<i class=verb><s>" + wordAnalyse(word) + "</s></i>";

                case "межд": return "<i class=union>" + wordAnalyse(word) + "</i>";
                case "союз": return "<b class=union>" + wordAnalyse(word) + "</b>";
                case "предл": return "<b class=prep>" + wordAnalyse(word) + "</b>";
                case "част": return "<b class=art>" + wordAnalyse(word) + "</b>";

                case "прч": return "<i class=part>" + wordAnalyse(word) + "</i>";
                case "предик": return "<i class=part><s>" + wordAnalyse(word) + "</s></i>";
                case "дееп": return "<b class=part>" + wordAnalyse(word) + "</b>";

                default:
                    if(type.contains("мест"))
                        return "<b class=pron>" + wordAnalyse(word) + "</b>";
//                    case "сущ,мест": return "<b class='noun'>" + wordAnalyse(word) + "</b>";
//                    case "прл,мест": return "<b class='adj'>" + wordAnalyse(word) + "</b>";
//                    case "нар,мест": return "<b class='verb'>" + wordAnalyse(word) + "</b>";
            }
        return "<a href='/'>"+wordAnalyse(word) + ":" + type+"</a>";
    }

    @Override
    public String toString() {
        return "{" + word + ":" +
                wordAnalyse(word)
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