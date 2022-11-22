import "dart:io";

void main() {
    
    print("Rentrer un nouveau user : ");
    print("name : ");
    String? name = stdin.readLineSync()!;
    print("age : ");
    int? age = int.parse(stdin.readLineSync()!);
    print("email : ");
    String? email = stdin.readLineSync()!;
    print("password : ");
    String? password = stdin.readLineSync()!;

    User newUser = User(name, age, email, password);
    print(newUser.toString());

}

class User {

    int? ID;
    String? name;
    int? age;
    String? email;
    String? password;
    static int currentID = 0;

    User(String name, int age, String email, String password) {

        this.ID = currentID;
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        currentID++;
    }

    String toString() {

        return "User : [ \n id = $ID \n name = $name \n age = $age \n email = $email \n password = $password \n ]";
    }
}