package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentDtoList {

    private int count;
    private List<CommentDto> list;

    public CommentDtoList(List<CommentDto> list) {
        this.list = list;
        this.count = list.size();
    }


}
