package controllers.AccountantControllers.invoice;

import models.entities.Invoice;
import javax.persistence.EntityManager;
import java.util.List;

public class InvoiceRepositoryImpl  implements InvoiceRepository {
    private final EntityManager em;

    public InvoiceRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        Invoice invoice = em.find(Invoice.class, id);
        em.detach(invoice);
        return invoice;
    }

    @Override
    public Invoice getInvoiceByNumber(String number) {
        Invoice invoice = em.find(Invoice.class, number);
        em.detach(invoice);
        return invoice;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        em.getTransaction().begin();
        if(invoice.getId() == null ){
            em.persist(invoice);
        } else {
            em.merge(invoice);
        }
        em.getTransaction().commit();
        return invoice;
    }

    @Override
    public void deleteInvoice(Invoice b) {

    }

    @Override
    public List<Invoice> getInvoiceList() {
        return em.createQuery("select e from Invoice e", Invoice.class)
                .getResultList();
    }
}

