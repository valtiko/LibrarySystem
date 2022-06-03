package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Book;
import models.entities.BookProposal;
import models.entities.Person;
import models.entities.Return;
import models.tables.BookProposalTable;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public class BookProposalRepository {
    DatabaseAccess databaseAccess;
    private static BookProposalRepository instance;

    public static BookProposalRepository getInstance() {
        if (instance == null) {
            instance = new BookProposalRepository();
        }
        return instance;
    }

    public BookProposalRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<BookProposal> getFullProposals() {
        String query = String.format("SELECT bp FROM BookProposal bp JOIN FETCH bp.person");
        List<BookProposal> proposals = proposalList(databaseAccess.getQuery(query));
        System.out.println(proposals);
        return proposals;
    }

    public List<BookProposalTable> getTableProposals() {
        List<BookProposalTable> proposals = new ArrayList<>();
        String query = String.format("select bp.author,bp.title,bp.person,bp.price,bp.quantity from BookProposal bp where status='PROPOSAL' order by bp.title");
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            Person person = (Person) o[2];
            BookProposalTable proposal = new BookProposalTable(
                    o[1].toString(),
                    o[0].toString(),
                    o[3].toString(),
                    o[4].toString(),
                    person.getName() + " " + person.getLastName());
            proposals.add(proposal);
        }
        return proposals;
    }

    private List<BookProposal> proposalList(List<Object> objects) {
        List<BookProposal> proposals = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof BookProposal) {
                Hibernate.initialize(o);
                ((BookProposal) o).setClientName(((BookProposal) o).getPerson().getName()
                        + " " + (((BookProposal) o).getPerson().getLastName()));
                proposals.add((BookProposal) o);
            }
        }
        return proposals;
    }

    public List<BookProposal> getProposalList(List<BookProposalTable> list){
        List<BookProposal> proposalList = new ArrayList<>();
        for (BookProposalTable o:list) {
            if(o != null){
                String[] split = o.getClientName().split(" ");
                String query = "select bk from BookProposal bk,Person p where p.name = '"+split[0]+"' and p.lastName='"
                        +split[1]+"' and author='"+ o.getAuthor()+"' and title='"+ o.getTitle()+"'";
                proposalList.add(getOneProposal(databaseAccess.getQuery(query)));
            }
        }
    return proposalList;
    }

    private BookProposal getOneProposal(List<Object> bookProposalList){
        if(!bookProposalList.isEmpty()) return (BookProposal) bookProposalList.get(0);
        return null;
    }

    public void updateBookProposal(BookProposal bookProposal) {
        databaseAccess.update(bookProposal);
    }

    public void createBookProposal(BookProposal bookProposal) {
        databaseAccess.save(bookProposal);
    }
}
