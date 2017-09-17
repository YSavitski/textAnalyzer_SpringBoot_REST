package com.example.springbootfileupload.components.brackets;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ArrayListStack<T> {
    private ArrayList<T> list = new ArrayList<>();

    public T pop(){
        if(!list.isEmpty()){
            return list.remove(list.size()-1);
        } else {
            throw new IndexOutOfBoundsException("Cannot pop an empty stack");
        }
    }

    public void push(T element){
        list.add(element);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void clearStack(){
        list.clear();
    }
}
