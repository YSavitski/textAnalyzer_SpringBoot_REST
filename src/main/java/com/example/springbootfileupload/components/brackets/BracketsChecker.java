package com.example.springbootfileupload.components.brackets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BracketsChecker {
    @Autowired
    private ArrayListStack<Character> stack;

    public boolean checkChars(String checkedString){
        char checkedChar;
        for (int i=0; i<checkedString.length(); i++){
            checkedChar = checkedString.charAt(i);
            switch (checkedChar){
                case '{': case '[': case '(':
                    stack.push(checkedChar);
                    break;
                case '}': case ']': case ')':
                    if(!stack.isEmpty()){
                        char popChar = stack.pop();
                        if( (checkedChar=='}' && popChar!='{') ||
                                (checkedChar==']' && popChar!='[') ||
                                (checkedChar==')' && popChar!='(') ) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public boolean checkIsEmptyStack(){
        return stack.isEmpty();
    }

    public void clearStack(){
        stack.clearStack();
    }
}
