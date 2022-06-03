package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Book;
import models.entities.NewBook;
import models.tables.NewBookTable;

import java.util.ArrayList;
import java.util.List;

public class NewBookRepository {
    DatabaseAccess databaseAccess;
    private static NewBookRepository instance;

    public static NewBookRepository getInstance() {
        if (instance == null) {
            instance = new NewBookRepository();
        }
        return instance;
    }

    public NewBookRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public void createNewBook(NewBook book){
        databaseAccess.save(book);
    }

    public void updateNewBook(NewBook book){
        databaseAccess.update(book);
    }

    public void deleteNewBook(NewBook book){
        databaseAccess.delete(book);
    }

    public List<NewBookTable> getNewBookList(){
        List<NewBookTable> newBookList = new ArrayList<>();
        String query = "select title,author,quantity from NewBook";
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o:objects) {
            NewBookTable newBook = new NewBookTable();
            newBook.setTitle(o[0].toString());
            newBook.setAuthor(o[1].toString());
            newBook.setQuantity(o[2].toString());
            newBookList.add(newBook);
        }
        return newBookList;
    }

    public NewBook getBook(String title,String author){
        String query = "from NewBook where title='"+title+"' and author='"+author+"'";
        return getOneBook(databaseAccess.getQuery(query));
    }

    private NewBook getOneBook(List<Object> objects){
        if(!objects.isEmpty()) return (NewBook) objects.get(0);
        return null;
    }

}
