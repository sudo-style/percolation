public class Stack{
    private int[] stack;
    private int top;
    private int size;
    public Stack(int size){
        this.size = size;
        stack = new int[size];
        top = -1;
    }
    public void push(int value){
        if(top == size - 1){
            System.out.println("Stack is full");
        }
        else{
            top++;
            stack[top] = value;
        }
    }
    public int pop(){
        if(top == -1){
            System.out.println("Stack is empty");
            return -1;
        }
        else{
            int value = stack[top];
            top--;
            return value;
        }
    }
    public int peek(){
        if(top == -1){
            System.out.println("Stack is empty");
            return -1;
        }
        else{
            return stack[top];
        }
    }
    public boolean isEmpty(){
        if(top == -1){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isFull(){
        if(top == size - 1){
            return true;
        }
        else{
            return false;
        }
    }
    public void printStack(){
        if(top == -1){
            System.out.println("Stack is empty");
        }
        else{
            for(int i = top; i >= 0; i--){
                System.out.println(stack[i]);
            }
        }
    }
    public static void main(String[] args){
        Stack stack = new Stack(5);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}