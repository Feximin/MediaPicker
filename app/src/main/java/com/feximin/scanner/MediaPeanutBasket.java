package com.feximin.scanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo on 16/1/30.
 */
public class MediaPeanutBasket<T extends AbstractMediaPeanut>  {

    private String path;
    private int num;
    private String name;
    private String albumPath ;   // 第一张图片作为封面
    private List<T> children = new ArrayList<>(1);

    public MediaPeanutBasket(){}

    public MediaPeanutBasket(String name){
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public List<T> getChildren() {
        return children;
    }

    public void add(T entity){
        children.add(entity);
        num ++;
    }

    public void clear(){
        children.clear();
        num = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaPeanutBasket<?> that = (MediaPeanutBasket<?>) o;

        return path != null ? path.equals(that.path) : that.path == null;

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
