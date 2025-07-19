/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 吴欣霖
 */

public class Music {
    private String path, title,artist,minutes;
    private double duration;
    public Music(String title, String artist, double duration, String path) {
        this.path = path;
        this.artist = artist;
        this.duration = duration;
        this.title = title;
        minutes = convertMin(duration);
    }
    private String convertMin(double duration){
        int min = (int)(duration/60);
        int sec = (int)(duration-(60*min));
        String secFormatted;
        if (String.valueOf(sec).length()<2){
            secFormatted = "0"+String.valueOf(sec);
        }
        else{
            secFormatted = String.valueOf(sec);
        }
        return String.valueOf(min)+":"+secFormatted;
    }

    public String getArtist() {
        return artist;
    }

    public double getDuration() {
        return duration;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
}
