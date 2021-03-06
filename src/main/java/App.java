import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here
        List<Item> items = itemRepository.findAll();
        Map<String, Item> itemMaps = items.stream().collect(Collectors.toMap(Item::getId, v -> v));
        List<String> bItems = getbItems(salesPromotionRepository.findAll());
        Map<Item, Integer> cart = new HashMap<>();

        for (String input : inputs) {
            String[] detail = input.replace(" ", "").split("x");
            cart.put(itemMaps.get(detail[0]), Integer.parseInt(detail[1]));
        }

        return buildResult(cart, bItems);
    }

    public List<String> getbItems(List<SalesPromotion> salesPromotions) {
        for (SalesPromotion salesPromotion : salesPromotions) {
            if (salesPromotion.getType().equals(Constant.DISCOUNT_50)) {
                return salesPromotion.getRelatedItems();
            }
        }
        return new ArrayList<>();
    }

    public String buildResult(Map<Item, Integer> cart, List<String> bItems) {
        StringBuffer result = new StringBuffer();
        double count = 0.0;
        double discount = 0.0;
        double totalPromotion = 0.0;
        boolean bSign = false;
        result.append("============= Order details =============\n");
        for (Item item : cart.keySet()) {
            double price = item.getPrice() * cart.get(item);
            double promotionPrice = price;
            if (bItems.contains(item.getId())) {
                bSign = true;
                discount += item.getPrice() / 2;
                promotionPrice = item.getPrice() / 2 * cart.get(item);
            }
            count += price;
            totalPromotion += promotionPrice;
            result.append(item.getName() + " x " + cart.get(item) + " = " + (int) price + " yuan\n");
        }
        result.append("-----------------------------------\n");
        if (count >= 30)
            count -= 6;

        if (bSign) {
            result.append("Promotion used:\n");
            if (count > totalPromotion) {
                result.append("Half price for certain dishes (Braised chicken，Cold noodles)，saving " + (int) discount + " yuan\n")
                        .append("-----------------------------------\n")
                        .append("Total：" + (int) totalPromotion + " yuan\n");
            } else {
                result.append("满30减6 yuan，saving 6 yuan\n")
                        .append("-----------------------------------\n")
                        .append("Total：" + (int) count + " yuan\n");
            }
        } else {
            result.append("Total：" + (int) count + " yuan\n");
        }

        result.append("===================================");
        return result.toString();
    }
}
