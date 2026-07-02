package com.ideaverse.stoiccard;

import java.util.Calendar;

final class Quotes {

    static final String[][] ALL = {
            {"The impediment to action advances action. What stands in the way becomes the way.", "Marcus Aurelius", "Meditations 5.20"},
            {"Waste no more time arguing about what a good man should be. Be one.", "Marcus Aurelius", "Meditations 10.16"},
            {"The best revenge is to be unlike him who performed the injury.", "Marcus Aurelius", "Meditations 6.6"},
            {"If it is not right, do not do it. If it is not true, do not say it.", "Marcus Aurelius", "Meditations 12.17"},
            {"Loss is nothing else but change, and change is nature's delight.", "Marcus Aurelius", "Meditations 9.35"},
            {"Do every act of your life as though it were the very last act of your life.", "Marcus Aurelius", "Meditations 2.5"},
            {"How much time he gains who does not look to see what his neighbour says or does or thinks.", "Marcus Aurelius", "Meditations 4.18"},
            {"The soul becomes dyed with the colour of its thoughts.", "Marcus Aurelius", "Meditations 5.16"},
            {"Receive without pride, let go without attachment.", "Marcus Aurelius", "Meditations 8.33"},
            {"Confine yourself to the present.", "Marcus Aurelius", "Meditations 7.29"},
            {"It is not death that a man should fear, but never beginning to live.", "Marcus Aurelius", "Meditations 12.1"},
            {"Very little is needed to make a happy life. It is all within yourself, in your way of thinking.", "Marcus Aurelius", "Meditations 7.67"},
            {"Some things are in our control and others not.", "Epictetus", "Enchiridion 1"},
            {"Men are disturbed not by things, but by the views which they take of things.", "Epictetus", "Enchiridion 5"},
            {"Do not demand that events happen as you wish. Wish them to happen as they do happen, and your life will be serene.", "Epictetus", "Enchiridion 8"},
            {"First say to yourself what you would be, and then do what you have to do.", "Epictetus", "Discourses 3.23"},
            {"Wealth consists not in having great possessions, but in having few wants.", "Epictetus", "Fragments"},
            {"If you want to improve, be content to be thought foolish and stupid.", "Epictetus", "Enchiridion 13"},
            {"No man is free who is not master of himself.", "Epictetus", "Fragments"},
            {"He is a wise man who does not grieve for the things which he has not, but rejoices for those which he has.", "Epictetus", "Fragments"},
            {"We suffer more often in imagination than in reality.", "Seneca", "Letters 13"},
            {"It is not that we have a short time to live, but that we waste much of it.", "Seneca", "On the Shortness of Life 1"},
            {"Difficulties strengthen the mind, as labour does the body.", "Seneca", "Letters 78"},
            {"Begin at once to live, and count each separate day as a separate life.", "Seneca", "Letters 101"},
            {"No man was ever wise by chance.", "Seneca", "Letters 76"},
            {"Associate with people who are likely to improve you.", "Seneca", "Letters 7"},
            {"It is a rough road that leads to the heights of greatness.", "Seneca", "Letters 84"},
            {"The whole future lies in uncertainty. Live immediately.", "Seneca", "On the Shortness of Life 9"},
            {"He suffers more than necessary, who suffers before it is necessary.", "Seneca", "Letters 98"},
            {"What fault have you cured today? What vice have you resisted? In what respect are you better?", "Seneca", "On Anger 3.36"}
    };

    private Quotes() {
    }

    static String[] today() {
        return ALL[Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % ALL.length];
    }
}
