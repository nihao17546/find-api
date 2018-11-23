package com.appcnd.find.api.pojo.json;

import lombok.Data;

/**
 * @author nihao 2018/11/23
 */
@Data
public class FontText {
    private String text;

    private int wm_text_pos;

    private String wm_text_color;

    private Integer wm_text_size;

    private String wm_text_font;//字体  “黑体，Arial”

    public FontText() {
    }

    public FontText(String text, int wm_text_pos, String wm_text_color, Integer wm_text_size, String wm_text_font) {
        this.text = text;
        this.wm_text_pos = wm_text_pos;
        this.wm_text_color = wm_text_color;
        this.wm_text_size = wm_text_size;
        this.wm_text_font = wm_text_font;
    }
}
