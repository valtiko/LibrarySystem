package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Book;
import models.entities.Borrow;
import models.entities.Person;
import models.entities.Return;
import models.tables.ReturnTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReturnRepository {
    DatabaseAccess databaseAccess;
    private static ReturnRepository instance;

    public static ReturnRepository getInstance() {
        if (instance == null) {
            instance = new ReturnRepository();
        }
        return instance;
    }

    public ReturnRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<ReturnTable> getTableReturns() {
        List<ReturnTable> returns = new ArrayList<>();
        String query = "select bk.bookName,bk.author,bw.endOfBorrowDate,pn.name,pn.lastName,bk.ISBN from Book bk,Borrow bw," +
                "Person pn,Return rn where bk.id=bw.book and pn.id=bw.person and rn.person is null and rn.borrow=bw.id";
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            ReturnTable returnTable = new ReturnTable();
            returnTable.setTittle(o[0].toString());
            returnTable.setAuthor(o[1].toString());
            returnTable.setEndOfBorrowDate((Date) o[2]);
            returnTable.setClient(o[3].toString()+" "+o[4].toString());
            returnTable.setISBN(o[5].toString());
            returns.add(returnTable);
        }
        return returns;
    }

    public List<Return> getFullReturns() {
        String query = String.format("from Return");
        List<Return> returns = returnList(databaseAccess.getQuery(query));
        System.out.println(returns);
        return returns;
    }

    public List<Return> returnList(List<Object> objects) {
        List<Return> reservations = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Return) {
                reservations.add((Return) o);
            }
        }
        return reservations;
    }

    public Return getReturn(Borrow borrow){
        String query = "from Return r where r.borrow="+borrow.getId();
        return getOneReturn(databaseAccess.getQuery(query));
    }

    private Return getOneReturn(List<Object> returnsList){
        if(!returnsList.isEmpty()) return (Return) returnsList.get(0);
        return null;
    }

    public void deleteReturn(Return ret) {
        databaseAccess.delete(ret);
    }

    public void updateReturn(Return ret){
        databaseAccess.update(ret);
    }

    public void createReturn(Return ret) {
        databaseAccess.save(ret);
    }
}
