import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class LotteryResult implements Serializable {

    private String date;
    private int draw_id;
    private boolean has_winner;
    private int id;
    private List<String> numbers;
    private double prize;
    private List<String> stars;

    public static LotteryResult fromEntry(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, LotteryResult.class);
    }

    public String getDate() {
        return date;
    }

    public int getDraw_id() {
        return draw_id;
    }

    public int getId() {
        return id;
    }

    public double getPrize() {
        return prize;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public List<String> getStars() {
        return stars;
    }
}
