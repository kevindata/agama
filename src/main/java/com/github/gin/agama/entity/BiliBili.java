package com.github.gin.agama.entity;

import com.github.gin.agama.annotation.ChildItem;
import com.github.gin.agama.annotation.Xpath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GinPonson on 10/15/2016.
 */
public class BiliBili extends HtmlEntity {

    @ChildItem(BiliBiliVedio.class)
    @Xpath("//div[@class='l-item']")
    private List<BiliBiliVedio> vedios = new ArrayList<>();

    public List<BiliBiliVedio> getVedios() {
        return vedios;
    }

    public void setVedios(List<BiliBiliVedio> vedios) {
        this.vedios = vedios;
    }
}