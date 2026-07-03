package com.ideaverse.stoiccard;

import java.util.Calendar;

/**
 * One quote per calendar date (366 incl. Feb 29), organized under 12 monthly
 * themes. Quotes are short renderings of public-domain translations of
 * Marcus Aurelius, Seneca, Epictetus and Musonius Rufus. Entry format:
 * {text, author, source}.
 */
final class Quotes {

    static final String[] THEMES = {
            "CLARITY", "EMOTION", "AWARENESS", "JUDGMENT",
            "ACTION", "OBSTACLES", "DUTY", "PRAGMATISM",
            "COURAGE", "VIRTUE", "ACCEPTANCE", "MORTALITY"
    };

    // Five journal prompts per month, rotated by day of month.
    private static final String[][] PROMPTS = {
            { // JAN clarity
                    "What actually matters today? Name one thing.",
                    "What did you chase today that was not yours to control?",
                    "Strip today to its essentials. What could you have skipped?",
                    "Where did your attention go today? Where should it have gone?",
                    "What would today look like if you did less, but deliberately?"
            },
            { // FEB emotion
                    "What disturbed you today, the thing itself or your view of it?",
                    "Which emotion drove you today? Did you drive it back?",
                    "What did you suffer today only in imagination?",
                    "Where did you pause before reacting today? Where didn't you?",
                    "What anger or worry can you set down before sleeping?"
            },
            { // MAR awareness
                    "What fault did you cure today? What vice did you resist?",
                    "What did you notice today that you usually walk past?",
                    "Where did you act on autopilot today?",
                    "What did today teach you about your own patterns?",
                    "Whose opinion pulled at you today? Did it deserve to?"
            },
            { // APR judgment
                    "What judgment did you add to a bare fact today?",
                    "What did you call bad today that was merely unexpected?",
                    "Where did first impressions mislead you today?",
                    "What story did you tell yourself today? Was it true?",
                    "What would this day look like described without opinion?"
            },
            { // MAY action
                    "What did you do today instead of talking about it?",
                    "What small right action did you take today?",
                    "Where did you hesitate today when you knew what to do?",
                    "What did you start today? What are you avoiding starting?",
                    "If today were repeated forever, what would you change?"
            },
            { // JUN obstacles
                    "What stood in your way today, and what way did it open?",
                    "What difficulty today was actually training?",
                    "What obstacle shrank today once you faced it directly?",
                    "Where did friction reveal something worth keeping?",
                    "What went wrong today that you can use tomorrow?"
            },
            { // JUL duty
                    "What did you owe today, and did you pay it?",
                    "Who depended on you today? How did you show up?",
                    "What common good did your work serve today?",
                    "Where did you do your part today without applause?",
                    "What duty are you postponing? What is the real cost?"
            },
            { // AUG pragmatism
                    "What worked today? Do more of it tomorrow.",
                    "Where did you insist on perfect instead of good today?",
                    "What did you want today that you already have?",
                    "What did you simplify today? What still needs it?",
                    "What resource did you waste today: time, words, or worry?"
            },
            { // SEP courage
                    "What did you do today despite fear?",
                    "Where did you choose comfort over the harder right thing?",
                    "What truth did you say or avoid saying today?",
                    "What risk worth taking are you still circling?",
                    "When were you last brave? What did it cost you, really?"
            },
            { // OCT virtue
                    "What did you do today that was simply good?",
                    "Were you today the person you claim to want to be?",
                    "What kindness did you give today? What did you withhold?",
                    "Who improved you today? Whom did you improve?",
                    "Where did your actions and your values disagree today?"
            },
            { // NOV acceptance
                    "What did you fight today that you should have accepted?",
                    "What happened today exactly as it should not have, and yet?",
                    "What did you release today? What are you still gripping?",
                    "What loss looked different today when seen as change?",
                    "What would loving today, as it was, actually mean?"
            },
            { // DEC mortality
                    "If today had been your last, what would have mattered?",
                    "What did you postpone today as if time were guaranteed?",
                    "What are you doing with the time you complain you lack?",
                    "What would you stop doing if you counted your remaining days?",
                    "What did you truly live today, rather than merely pass through?"
            }
    };

    private static final String[][] JAN = {
            {"Some things are in our control and others not.", "Epictetus", "Enchiridion 1"},
            {"Very little is needed to make a happy life. It is all within yourself, in your way of thinking.", "Marcus Aurelius", "Meditations 7.67"},
            {"Confine yourself to the present.", "Marcus Aurelius", "Meditations 7.29"},
            {"If you seek tranquillity, do less. Do what is essential.", "Marcus Aurelius", "Meditations 4.24"},
            {"Ask yourself at every moment: is this necessary?", "Marcus Aurelius", "Meditations 4.24"},
            {"To be everywhere is to be nowhere.", "Seneca", "Letters 2"},
            {"If a man knows not to which port he sails, no wind is favourable.", "Seneca", "Letters 71"},
            {"The first task of philosophy is to throw away self-conceit; no one can learn what he thinks he already knows.", "Epictetus", "Discourses 2.17"},
            {"Where a man can live, there he can also live well.", "Marcus Aurelius", "Meditations 5.16"},
            {"Attend to what is your own; what belongs to others will never be yours.", "Epictetus", "Enchiridion 1"},
            {"Nowhere can a man find a quieter retreat than in his own soul.", "Marcus Aurelius", "Meditations 4.3"},
            {"Do not act as if you had ten thousand years to live.", "Marcus Aurelius", "Meditations 4.17"},
            {"The greater part of progress is the desire to progress.", "Seneca", "Letters 71"},
            {"First learn the meaning of what you say, and then speak.", "Epictetus", "Discourses 3.23"},
            {"A good character is the only guarantee of everlasting, carefree happiness.", "Seneca", "Letters 27"},
            {"Look within. Within is the fountain of good, ever ready to bubble up, if you will ever dig.", "Marcus Aurelius", "Meditations 7.59"},
            {"It is in your power to retire into yourself whenever you choose.", "Marcus Aurelius", "Meditations 4.3"},
            {"No man can swim ashore and carry his baggage with him.", "Seneca", "Letters 22"},
            {"Know first who you are, and then adorn yourself accordingly.", "Epictetus", "Discourses 3.1"},
            {"Nothing is worth doing pointlessly.", "Marcus Aurelius", "Meditations 2.7"},
            {"Let all your efforts be directed to something, let them keep something in view.", "Seneca", "On Tranquility of Mind 12"},
            {"The essence of philosophy is that a man should so live that his happiness depends as little as possible on external things.", "Epictetus", "Fragments"},
            {"He who is everywhere is nowhere at home.", "Seneca", "Letters 2"},
            {"Do not spend your remaining years worrying about other people, unless it serves the common good.", "Marcus Aurelius", "Meditations 3.4"},
            {"You become what you give your attention to.", "Epictetus", "Discourses 4.4"},
            {"Each day acquire something that will fortify you against poverty, against death, against other misfortunes.", "Seneca", "Letters 2"},
            {"Nothing, in my opinion, is a better proof of a well-ordered mind than a man's ability to stop just where he is and pass some time in his own company.", "Seneca", "Letters 2"},
            {"Think of the whole of existence, of which you are the smallest part.", "Marcus Aurelius", "Meditations 5.24"},
            {"Do not let the future disturb you. You will meet it, if you must, with the same reason you now use for the present.", "Marcus Aurelius", "Meditations 7.8"},
            {"Never call yourself a philosopher, nor talk much among the unlearned about principles; act on them.", "Epictetus", "Enchiridion 46"},
            {"The happy life is to have a mind that is free, upright, and unafraid, standing outside the reach of fear and of desire.", "Seneca", "On the Happy Life 4"}
    };

    private static final String[][] FEB = {
            {"We suffer more often in imagination than in reality.", "Seneca", "Letters 13"},
            {"Men are disturbed not by things, but by the views which they take of things.", "Epictetus", "Enchiridion 5"},
            {"He suffers more than necessary, who suffers before it is necessary.", "Seneca", "Letters 98"},
            {"The greatest remedy for anger is delay.", "Seneca", "On Anger 2.29"},
            {"How much heavier are the consequences of anger than its causes.", "Marcus Aurelius", "Meditations 11.18"},
            {"No man is free who is not master of himself.", "Epictetus", "Fragments"},
            {"When you are offended at any man's fault, turn to yourself and study your own failings.", "Epictetus", "Fragments"},
            {"It is not the man who has too little, but the man who craves more, that is poor.", "Seneca", "Letters 2"},
            {"Anger, if not restrained, is frequently more hurtful to us than the injury that provokes it.", "Seneca", "On Anger 1.12"},
            {"You have power over your mind, not outside events. Realise this, and you will find strength.", "Marcus Aurelius", "Meditations 12.22"},
            {"Fear keeps pace with hope; both belong to a mind in suspense, hanging on what the future may bring.", "Seneca", "Letters 5"},
            {"If you are pained by any external thing, it is not the thing that disturbs you, but your own judgment about it.", "Marcus Aurelius", "Meditations 8.47"},
            {"Any person capable of angering you becomes your master.", "Epictetus", "Enchiridion (after 20)"},
            {"Choose not to be harmed, and you will not feel harmed. Do not feel harmed, and you have not been.", "Marcus Aurelius", "Meditations 4.7"},
            {"Count your years, and you will be ashamed to desire and pursue the same things you desired in your boyhood.", "Seneca", "Letters 27"},
            {"It is not events themselves that trouble people, but their judgments about events.", "Epictetus", "Enchiridion 5"},
            {"The mind that is anxious about future events is miserable.", "Seneca", "Letters 98"},
            {"Remember that it is we who torment, we who make difficulties for ourselves.", "Epictetus", "Discourses 1.25"},
            {"A real man does not give way to anger and discontent; such a person has strength, courage, and endurance.", "Marcus Aurelius", "Meditations 11.18"},
            {"Reflect that the chief source of all evils to man is not death, but the fear of death.", "Epictetus", "Discourses 3.26"},
            {"Nothing is burdensome if taken lightly, and nothing need arouse one's irritation so long as one does not make it bigger than it is.", "Seneca", "On Anger 3.11"},
            {"Today I escaped anxiety. Or no, I discarded it, because it was within me, in my own perceptions.", "Marcus Aurelius", "Meditations 9.13"},
            {"He is most powerful who has power over himself.", "Seneca", "Letters 90"},
            {"Sickness is a hindrance to the body, but not to your ability to choose, unless you choose so.", "Epictetus", "Enchiridion 9"},
            {"There is no evil in things changing, just as there is no good in persisting in a new state.", "Marcus Aurelius", "Meditations 4.42"},
            {"What need is there to weep over parts of life? The whole of it calls for tears.", "Seneca", "Letters 4 (attr.)"},
            {"Freedom is secured not by the fulfilling of desires, but by the removal of desire.", "Epictetus", "Discourses 4.1"},
            {"Give yourself a gift: the present moment.", "Marcus Aurelius", "Meditations 8.44"},
            {"Limit your desires, and you limit your fears.", "Seneca", "Letters 5"}
    };

    private static final String[][] MAR = {
            {"What fault have you cured today? What vice have you resisted? In what respect are you better?", "Seneca", "On Anger 3.36"},
            {"The soul becomes dyed with the colour of its thoughts.", "Marcus Aurelius", "Meditations 5.16"},
            {"How much time he gains who does not look to see what his neighbour says or does or thinks.", "Marcus Aurelius", "Meditations 4.18"},
            {"No man was ever wise by chance.", "Seneca", "Letters 76"},
            {"We should every night call ourselves to an account: what infirmity have I mastered today?", "Seneca", "On Anger 3.36"},
            {"Look well into yourself; there is a source of strength which will always spring up if you will always look.", "Marcus Aurelius", "Meditations 7.59"},
            {"It is impossible for a man to learn what he thinks he already knows.", "Epictetus", "Discourses 2.17"},
            {"Every night before going to sleep, we must ask ourselves: what weakness did I overcome today? What virtue did I acquire?", "Seneca", "On Anger 3.36"},
            {"Watch the stars in their courses, and see yourself running with them.", "Marcus Aurelius", "Meditations 7.47"},
            {"If you wish to be a writer, write.", "Epictetus", "Discourses 2.18 (attr.)"},
            {"Nothing is more hostile to a firm grasp on knowledge than self-deception.", "Epictetus", "Discourses 2.17"},
            {"Observe the movements of your own mind; nothing of another's escapes you, yet your own you do not watch.", "Marcus Aurelius", "Meditations 10.37 (attr.)"},
            {"Retire into yourself as much as you can; associate with those likely to improve you.", "Seneca", "Letters 7"},
            {"Men seek retreats for themselves: houses in the country, seashores, mountains. But this is a mark of the most common sort of men.", "Marcus Aurelius", "Meditations 4.3"},
            {"You are a little soul carrying about a corpse, as Epictetus used to say.", "Marcus Aurelius", "Meditations 4.41"},
            {"When you have been compelled by circumstances to be disturbed, return to yourself quickly.", "Marcus Aurelius", "Meditations 6.11"},
            {"Most of what we say and do is not essential. If you can eliminate it, you will have more time and more tranquillity.", "Marcus Aurelius", "Meditations 4.24"},
            {"The unexamined life drifts; the examined one steers.", "Epictetus", "Discourses (paraphrase)"},
            {"Everything we hear is an opinion, not a fact. Everything we see is a perspective, not the truth.", "Marcus Aurelius", "Meditations (attr.)"},
            {"Each man's mind is his proper guardian spirit; keep it pure.", "Marcus Aurelius", "Meditations 5.27"},
            {"When you arise in the morning, think of what a precious privilege it is to be alive: to breathe, to think, to enjoy, to love.", "Marcus Aurelius", "Meditations (attr.)"},
            {"Nothing is so certain as that the evils of idleness can be shaken off by hard work.", "Seneca", "On Tranquility of Mind 4"},
            {"Wherever I go, it will be well with me, for it was well with me here, not on account of the place, but of my judgments.", "Epictetus", "Discourses 4.7"},
            {"Time discovers truth.", "Seneca", "On Anger 2.22"},
            {"As is a tale, so is life: not how long it is, but how good it is, is what matters.", "Seneca", "Letters 77"},
            {"Look beneath the surface; let not the quality of a thing nor its worth escape you.", "Marcus Aurelius", "Meditations 6.3"},
            {"Practise yourself, for heaven's sake, in little things, and thence proceed to greater.", "Epictetus", "Discourses 1.18"},
            {"A consciousness of wrongdoing is the first step to salvation; you must catch yourself in it before you can correct it.", "Seneca", "Letters 28"},
            {"Short is the little that remains to you of life. Live as on a mountain.", "Marcus Aurelius", "Meditations 10.15"},
            {"He who studies with a philosopher should take away with him some one good thing every day.", "Seneca", "Letters 108"},
            {"From now on, then, resolve to live as a grown-up who is making progress.", "Epictetus", "Enchiridion 51"}
    };

    private static final String[][] APR = {
            {"If it is not right, do not do it. If it is not true, do not say it.", "Marcus Aurelius", "Meditations 12.17"},
            {"Everything is what your opinion makes it, and that opinion lies with yourself.", "Marcus Aurelius", "Meditations 12.22"},
            {"It is not things that disturb us, but our judgments about things.", "Epictetus", "Enchiridion 5"},
            {"Take away your opinion, and there is taken away the complaint 'I have been harmed.'", "Marcus Aurelius", "Meditations 4.7"},
            {"We are more often frightened than hurt; and we suffer more from opinion than from reality.", "Seneca", "Letters 13"},
            {"Do not judge, and you will not be judged wrongly by yourself.", "Epictetus", "Discourses (paraphrase)"},
            {"Say of nothing 'I have lost it,' but 'I have given it back.'", "Epictetus", "Enchiridion 11"},
            {"Things do not touch the soul; our perturbations come only from the opinion which is within.", "Marcus Aurelius", "Meditations 4.3"},
            {"What is quite unlooked for is more crushing in its effect; reflect on everything that may happen.", "Seneca", "Letters 91"},
            {"When someone provokes you, remember it is your own judgment that provokes you.", "Epictetus", "Enchiridion 20"},
            {"Everywhere and at all times it is in your power to be reverently content with your present condition.", "Marcus Aurelius", "Meditations 7.54"},
            {"He who fears death will never do anything worthy of a living man.", "Seneca", "Letters 26"},
            {"Do not let a picture of your whole life together confuse you; deal with what is at hand.", "Marcus Aurelius", "Meditations 8.36"},
            {"It is the act of an ill-instructed man to blame others for his own bad condition.", "Epictetus", "Enchiridion 5"},
            {"Instruction begins when you blame yourself; wisdom, when you blame neither others nor yourself.", "Epictetus", "Enchiridion 5"},
            {"Judge a horse by its speed, a dog by its scent, and a man by his own work, not his fortune.", "Epictetus", "Enchiridion 44 (adapted)"},
            {"These things you see change instantly and will no longer be; keep in mind how many changes you have already witnessed.", "Marcus Aurelius", "Meditations 4.3"},
            {"Nothing deceives us more than our own judgment when it flatters us.", "Seneca", "Letters (paraphrase)"},
            {"Appearances to the mind are of four kinds: things are what they appear to be, or neither are nor appear, or are but do not appear, or are not yet appear.", "Epictetus", "Discourses 1.27"},
            {"The object of life is not to be on the side of the majority, but to escape finding oneself in the ranks of the insane.", "Marcus Aurelius", "Meditations (attr.)"},
            {"Praise or blame from a crowd should weigh nothing; the crowd praises today what it condemns tomorrow.", "Seneca", "Letters 7 (adapted)"},
            {"Whenever you find yourself blaming providence, turn it about in your mind and you will see the thing has happened according to reason.", "Epictetus", "Discourses 3.17"},
            {"Do not say more to yourself than the first appearances report.", "Marcus Aurelius", "Meditations 8.49"},
            {"Someone bathes hastily; do not say he bathes badly, but hastily. You see the appearance, not the whole.", "Epictetus", "Enchiridion 45"},
            {"Cucumber is bitter? Throw it away. There are briars in the road? Turn aside. That is enough; do not add, 'Why were such things made?'", "Marcus Aurelius", "Meditations 8.50"},
            {"How ridiculous and how strange to be surprised at anything which happens in life.", "Marcus Aurelius", "Meditations 12.13"},
            {"To accuse others for one's own misfortunes is a sign of want of education.", "Epictetus", "Enchiridion 5"},
            {"That which is not good for the swarm is not good for the bee.", "Marcus Aurelius", "Meditations 6.54"},
            {"Reflect on how much more painful are the consequences of our judgments than the events themselves.", "Marcus Aurelius", "Meditations 11.18 (adapted)"},
            {"Life is neither good nor evil, but only a place for good and evil.", "Seneca", "Letters 99"}
    };

    private static final String[][] MAY = {
            {"Waste no more time arguing about what a good man should be. Be one.", "Marcus Aurelius", "Meditations 10.16"},
            {"First say to yourself what you would be, and then do what you have to do.", "Epictetus", "Discourses 3.23"},
            {"Begin at once to live, and count each separate day as a separate life.", "Seneca", "Letters 101"},
            {"Do every act of your life as though it were the very last act of your life.", "Marcus Aurelius", "Meditations 2.5"},
            {"While we are postponing, life speeds by.", "Seneca", "Letters 1"},
            {"Progress is not achieved by luck or accident, but by working on yourself daily.", "Epictetus", "Discourses (paraphrase)"},
            {"At dawn, when you have trouble getting out of bed, tell yourself: I am rising to do the work of a human being.", "Marcus Aurelius", "Meditations 5.1"},
            {"Nothing great is created suddenly, any more than a bunch of grapes or a fig.", "Epictetus", "Discourses 1.15"},
            {"Let philosophy scrape off your own faults, rather than be a way to rail against the faults of others.", "Seneca", "Letters 103"},
            {"If anyone can refute me and show me my error, I will gladly change; I seek the truth.", "Marcus Aurelius", "Meditations 6.21"},
            {"Practise even what you have despaired of mastering.", "Marcus Aurelius", "Meditations 12.6"},
            {"How long will you wait before you demand the best of yourself?", "Epictetus", "Enchiridion 51"},
            {"Do what nature demands now; set yourself in motion, and do not look about to see whether anyone will observe it.", "Marcus Aurelius", "Meditations 9.29"},
            {"It is not because things are difficult that we do not dare; it is because we do not dare that they are difficult.", "Seneca", "Letters 104"},
            {"No labour is a good in itself; the good is labour directed at a worthy end.", "Seneca", "Letters 31 (adapted)"},
            {"Whatever you would make habitual, practise it; and if you would not make a thing habitual, do not practise it.", "Epictetus", "Discourses 2.18"},
            {"Work yourself hard, but not as if you were being made a victim; seek not to be pitied or admired.", "Marcus Aurelius", "Meditations 7.73 (adapted)"},
            {"Show me a man who cares how he acts rather than how he talks, and I will show you progress.", "Epictetus", "Discourses 1.4 (adapted)"},
            {"You must be one man, either good or bad; cultivate either your own ruling faculty or externals.", "Epictetus", "Discourses 3.15"},
            {"Life is long enough if you know how to use it.", "Seneca", "On the Shortness of Life 2"},
            {"That which is really beautiful needs no praise; is an emerald made worse if it is not praised?", "Marcus Aurelius", "Meditations 4.20"},
            {"Do not explain your philosophy. Embody it.", "Epictetus", "Enchiridion 46 (adapted)"},
            {"Every hour of the day give vigorous attention to the performance of the task in hand, with strict justice, and free from distraction.", "Marcus Aurelius", "Meditations 2.5"},
            {"A gem cannot be polished without friction, nor a man perfected without trials.", "Seneca", "On Providence (adapted)"},
            {"Let deeds match words: only he keeps his promise who is the same wherever you see him.", "Seneca", "Letters 20"},
            {"He who has begun has half done. Dare to be wise; begin.", "Seneca", "Letters (attr.)"},
            {"On the occasion of every act, ask yourself: how does this stand with respect to me? Shall I repent of it?", "Marcus Aurelius", "Meditations 8.2"},
            {"Sheep do not vomit up their grass to show the shepherds how much they have eaten; they digest it, and produce wool and milk. Show your principles digested, in acts.", "Epictetus", "Enchiridion 46"},
            {"What we plant in the soil of contemplation, we shall reap in the harvest of action.", "Seneca", "Letters (paraphrase)"},
            {"Not to feel exasperated or defeated when your days are not packed with wise and moral actions, but to get back up when you fail.", "Marcus Aurelius", "Meditations 5.9"},
            {"It is not enough to know; you must apply. It is not enough to will; you must do.", "Epictetus", "Discourses (paraphrase)"}
    };

    private static final String[][] JUN = {
            {"The impediment to action advances action. What stands in the way becomes the way.", "Marcus Aurelius", "Meditations 5.20"},
            {"Difficulties strengthen the mind, as labour does the body.", "Seneca", "Letters 78"},
            {"It is a rough road that leads to the heights of greatness.", "Seneca", "Letters 84"},
            {"The greater the difficulty, the more glory in surmounting it.", "Epictetus", "Discourses (attr.)"},
            {"Fire tests gold; misfortune tests brave men.", "Seneca", "On Providence 5"},
            {"What would have become of Hercules, do you think, if there had been no lion, hydra, stag or boar?", "Epictetus", "Discourses 1.6"},
            {"A setback has often cleared the way for greater prosperity.", "Seneca", "Letters 91 (adapted)"},
            {"The art of life is more like the wrestler's art than the dancer's: stand ready and firm to meet sudden and unexpected onsets.", "Marcus Aurelius", "Meditations 7.61"},
            {"No tree becomes rooted and sturdy unless many a wind assails it.", "Seneca", "On Providence 4"},
            {"Difficulties are things that show what men are.", "Epictetus", "Discourses 1.24"},
            {"It is not the load, but the way you carry it, that breaks you.", "Seneca", "Letters (attr.)"},
            {"You must either instruct men or bear with them.", "Marcus Aurelius", "Meditations 8.59"},
            {"When a difficulty falls upon you, remember that God, like a trainer of wrestlers, has matched you with a rough young man.", "Epictetus", "Discourses 1.24"},
            {"Nothing happens to any man which he is not formed by nature to bear.", "Marcus Aurelius", "Meditations 5.18"},
            {"I judge you unfortunate because you have never lived through misfortune; you have passed through life without an opponent.", "Seneca", "On Providence 4"},
            {"Every difficulty in life presents us with an opportunity to turn inward and to invoke our own inner resources.", "Epictetus", "Discourses (paraphrase)"},
            {"That which does not kill the flame feeds it: an obstacle to a great fire becomes its fuel.", "Marcus Aurelius", "Meditations 4.1 (adapted)"},
            {"A ship should not ride on a single anchor, nor life on a single hope.", "Epictetus", "Fragments"},
            {"Bear and forbear.", "Epictetus", "Fragments"},
            {"Prosperity comes to the mob as well; but to triumph over the disasters of mortal life is the privilege of a great man.", "Seneca", "On Providence 4"},
            {"Do not pray for an easy life; pray for the strength to endure a difficult one.", "Epictetus", "Discourses (paraphrase)"},
            {"Loss is nothing else but change, and change is nature's delight.", "Marcus Aurelius", "Meditations 9.35"},
            {"Adversity is the occasion of virtue: calamity gives us the chance to show courage.", "Seneca", "On Providence 4 (adapted)"},
            {"If you are hindered in one road, another opens; the universe is full of ways for the reasonable creature.", "Marcus Aurelius", "Meditations 5.20 (adapted)"},
            {"He who has practised against hardship in advance will bear it best when it comes.", "Seneca", "Letters 18 (adapted)"},
            {"To bear trials with a calm mind robs misfortune of its strength and burden.", "Seneca", "Hercules Oetaeus (attr.)"},
            {"An obstacle in our path is material for the virtues of patience and courage.", "Marcus Aurelius", "Meditations 5.20 (adapted)"},
            {"Never say about anything, 'I have lost it,' only 'I have returned it.'", "Epictetus", "Enchiridion 11"},
            {"The bull does not become a bull all at once, nor a man brave all at once; we must train through the winter.", "Epictetus", "Discourses 1.2"},
            {"Constant misfortune brings this one blessing: those whom it always assails, it at last fortifies.", "Seneca", "On Providence 4"}
    };

    private static final String[][] JUL = {
            {"What is not good for the hive is not good for the bee.", "Marcus Aurelius", "Meditations 6.54"},
            {"Men exist for the sake of one another. Teach them then, or bear with them.", "Marcus Aurelius", "Meditations 8.59"},
            {"Do your duty, and do not concern yourself with whether it is cold or warm, whether you are drowsy or well rested.", "Marcus Aurelius", "Meditations 6.2 (adapted)"},
            {"We are made for cooperation, like feet, like hands, like eyelids, like the rows of the upper and lower teeth.", "Marcus Aurelius", "Meditations 2.1"},
            {"Wherever a man is placed, there he must do the work of a man.", "Epictetus", "Discourses 1.16 (adapted)"},
            {"No one is a citizen of one city only; we are citizens of the world.", "Epictetus", "Discourses 1.9 (adapted)"},
            {"Live for others if you would live for yourself.", "Seneca", "Letters 48"},
            {"What is your vocation? To be a good person.", "Marcus Aurelius", "Meditations 11.5"},
            {"Duties are universally measured by relations. Is a man a father? The duty is to take care of him, to yield to him in all things.", "Epictetus", "Enchiridion 30"},
            {"That which is done for the common good is never a loss to the doer.", "Marcus Aurelius", "Meditations (paraphrase)"},
            {"A man's true delight is to do the things he was made for.", "Marcus Aurelius", "Meditations 8.26 (adapted)"},
            {"Remember that you are an actor in a play the author chooses: if short, then short; if long, then long. Act your part well.", "Epictetus", "Enchiridion 17"},
            {"Nature gives me a post to hold; I must not desert it.", "Epictetus", "Discourses 1.9 (adapted)"},
            {"Let your one delight and refreshment be to pass from one service to the community to another.", "Marcus Aurelius", "Meditations 6.7"},
            {"He who does a kindness to another does a kindness to himself.", "Seneca", "Letters 81 (adapted)"},
            {"When you have done a good act, what more do you require? Is it not enough to have acted according to your nature?", "Marcus Aurelius", "Meditations 9.42"},
            {"Some men, when they have done you a service, are forward to set it down to your account. A third kind does not even know what he has done, like a vine that has produced grapes.", "Marcus Aurelius", "Meditations 5.6"},
            {"No school has more goodness and gentleness, more love of humanity and attention to the common good, than Stoicism.", "Seneca", "On Clemency 2.5 (adapted)"},
            {"What brings no benefit to the community brings none to the citizen.", "Marcus Aurelius", "Meditations 5.22 (adapted)"},
            {"The wise man belongs to his country and to humanity; his service is never done.", "Seneca", "On Leisure (adapted)"},
            {"Wherever there is a human being, there is an opportunity for a kindness.", "Seneca", "On the Happy Life 24 (adapted)"},
            {"Do not tire of receiving help; it is no shame to be aided in your duty, like a soldier in a storming party.", "Marcus Aurelius", "Meditations 7.7 (adapted)"},
            {"Man is born for deeds of kindness.", "Marcus Aurelius", "Meditations 9.42 (adapted)"},
            {"To expect bad men not to do wrong is madness; to allow them to wrong others and expect exemption for yourself is unreasonable.", "Marcus Aurelius", "Meditations 9.42 (adapted)"},
            {"The universe is a kind of commonwealth, and we are its citizens.", "Epictetus", "Discourses 2.5 (adapted)"},
            {"Do not be ashamed of help; your business is to do your appointed duty, like a soldier in the breach.", "Marcus Aurelius", "Meditations 7.7"},
            {"Every place is safe to him who lives with justice.", "Epictetus", "Fragments"},
            {"Where life is, there duty is; and where duty is done, life is well spent.", "Musonius Rufus", "Lectures (paraphrase)"},
            {"It is peculiar to man to love even those who do wrong.", "Marcus Aurelius", "Meditations 7.22"},
            {"Do not do your duty grudgingly, like an unwilling ox; man was made to work with others as the hands work together.", "Marcus Aurelius", "Meditations 2.1 (adapted)"},
            {"That which is your duty, go to it though it be through fire and frost.", "Epictetus", "Discourses (paraphrase)"}
    };

    private static final String[][] AUG = {
            {"Wealth consists not in having great possessions, but in having few wants.", "Epictetus", "Fragments"},
            {"It is not the man who has too little, but the man who craves more, who is poor.", "Seneca", "Letters 2"},
            {"Receive without pride, let go without attachment.", "Marcus Aurelius", "Meditations 8.33"},
            {"Do not seek to have events happen as you want them to, but instead want them to happen as they do happen.", "Epictetus", "Enchiridion 8"},
            {"He is a wise man who does not grieve for the things which he has not, but rejoices for those which he has.", "Epictetus", "Fragments"},
            {"Until we have begun to go without them, we fail to realise how unnecessary many things are.", "Seneca", "Letters 123"},
            {"Nothing is enough for the man to whom enough is too little.", "Epictetus", "Fragments"},
            {"It is in no man's power to have whatever he wants; but he has it in his power not to wish for what he has not.", "Seneca", "Letters 123"},
            {"Cling to what is within your power: opinion, impulse, desire, aversion. The rest is not yours.", "Epictetus", "Enchiridion 1 (adapted)"},
            {"If you wish to make progress, be content to appear stupid and foolish in externals.", "Epictetus", "Enchiridion 13"},
            {"That man is richest whose pleasures are cheapest.", "Seneca", "Letters (paraphrase)"},
            {"Remember that you must behave in life as at a dinner party: something is carried around, and it is opposite you. Stretch out your hand and take a portion with decency.", "Epictetus", "Enchiridion 15"},
            {"He who needs least has the most freedom.", "Musonius Rufus", "Lectures (paraphrase)"},
            {"Riches are not forbidden, but the pride of them is.", "Seneca", "On the Happy Life 22 (adapted)"},
            {"No man is crushed by fortune unless he is first deceived by her.", "Seneca", "On Tranquility of Mind 11 (adapted)"},
            {"Frugality is a great revenue.", "Seneca", "Letters (attr.)"},
            {"Adapt yourself to the things among which your lot has been cast, and love the men with whom fate has brought you together.", "Marcus Aurelius", "Meditations 6.39"},
            {"You act like mortals in all that you fear, and like immortals in all that you desire.", "Seneca", "On the Shortness of Life 3"},
            {"Asked who is the rich man, Epictetus replied: he who is content.", "Epictetus", "Fragments (adapted)"},
            {"A man is as unhappy as he has convinced himself he is.", "Seneca", "Letters 78"},
            {"Slavery is the condition of him who cannot do without what he thinks he cannot do without.", "Seneca", "Letters 77 (adapted)"},
            {"The measure of a man is the worth of the things he cares about.", "Marcus Aurelius", "Meditations 7.3 (adapted)"},
            {"He is not poor who has little, but he who desires more.", "Seneca", "Letters 2 (adapted)"},
            {"Prefer enduring hardship to comfort bought at the price of your freedom.", "Musonius Rufus", "Lectures (paraphrase)"},
            {"What is the fruit of your principles? Tranquillity, fearlessness, freedom.", "Epictetus", "Discourses 1.4 (adapted)"},
            {"Take the shortest road: the road according to nature.", "Marcus Aurelius", "Meditations 4.51"},
            {"Let us prepare our minds as if we had come to the very end of life; let us balance life's books each day.", "Seneca", "Letters 101"},
            {"Never value anything as profitable to you which compels you to break your promise, to lose your self-respect.", "Marcus Aurelius", "Meditations 3.7"},
            {"To live happily is the same thing as to live according to nature.", "Seneca", "On the Happy Life 8 (adapted)"},
            {"He has most who is most content with the least.", "Seneca", "Letters (paraphrase)"},
            {"Simplify. Most of what you carry, you do not need.", "Marcus Aurelius", "Meditations 4.24 (paraphrase)"}
    };

    private static final String[][] SEP = {
            {"If you want to improve, be content to be thought foolish and stupid.", "Epictetus", "Enchiridion 13"},
            {"It is not because things are difficult that we do not dare; it is because we do not dare that things are difficult.", "Seneca", "Letters 104"},
            {"Life without the courage for death is slavery.", "Seneca", "Letters 77 (adapted)"},
            {"He who fears he shall suffer, already suffers what he fears.", "Seneca", "Letters (attr.)"},
            {"Do not fear the future; you will meet it, if you must, armed with the same reason you use today.", "Marcus Aurelius", "Meditations 7.8 (adapted)"},
            {"Fortune favours the brave: the timid man builds his own prison.", "Seneca", "Medea (adapted)"},
            {"What ought one to say as each hardship comes? I was practising for this, I was training for this.", "Epictetus", "Discourses 3.10"},
            {"Every habit and faculty is preserved and increased by the corresponding actions: walking by walking, running by running, courage by acts of courage.", "Epictetus", "Discourses 2.18 (adapted)"},
            {"Dare to look up to God and say: use me henceforward for whatever you will. I refuse nothing that seems good to you.", "Epictetus", "Discourses 2.16"},
            {"He who is brave is free.", "Seneca", "Letters (attr.)"},
            {"Do not pray for things to be otherwise, but for the strength to meet them as they are.", "Epictetus", "Enchiridion 8 (adapted)"},
            {"A brave man is not one who does not feel afraid, but one who conquers that fear.", "Seneca", "Letters (paraphrase)"},
            {"Sometimes even to live is an act of courage.", "Seneca", "Letters 78"},
            {"No man is more unhappy than he who never faces adversity, for he is not permitted to prove himself.", "Seneca", "On Providence 4 (adapted)"},
            {"Difficulty shows what men are. Henceforth, when a difficulty falls upon you, remember you are the wrestler.", "Epictetus", "Discourses 1.24 (adapted)"},
            {"Nothing befalls a man which he is not fitted by nature to bear.", "Marcus Aurelius", "Meditations 5.18"},
            {"It is more civilised to laugh at life than to lament over it.", "Seneca", "On Tranquility of Mind 15"},
            {"Where fear is, happiness is not.", "Seneca", "Letters (paraphrase)"},
            {"The one who does wrong out of fear is doubly a slave.", "Epictetus", "Discourses (paraphrase)"},
            {"Do not be ashamed of doing right merely because many disapprove.", "Musonius Rufus", "Lectures (paraphrase)"},
            {"If evils are coming, why run to meet them? You will suffer soon enough when they arrive.", "Seneca", "Letters 13 (adapted)"},
            {"Cowardice asks: is it safe? Reason asks: is it right?", "Epictetus", "Discourses (paraphrase)"},
            {"A man's worth is no greater than the worth of his ambitions.", "Marcus Aurelius", "Meditations 7.3 (adapted)"},
            {"He who spares the wicked injures the good.", "Seneca", "Fragments (attr.)"},
            {"Which of you does not prefer to be blind rather than to be deceived? Then defend your judgment as you would your eyes.", "Epictetus", "Discourses 1.28 (adapted)"},
            {"To bear one's fate bravely, and not to be crushed by circumstance: this is the greatness of the soul.", "Seneca", "On Providence 5 (paraphrase)"},
            {"You will find rest from vain fancies if you do every act in life as if it were your last.", "Marcus Aurelius", "Meditations 2.5"},
            {"He is a man of courage who does not fear an honourable death.", "Seneca", "Letters 76 (adapted)"},
            {"Man is not worried by real problems so much as by his imagined anxieties about real problems.", "Epictetus", "Discourses (attr.)"},
            {"Stand firm, like a rock against which the waves break continually; it stands, and the water grows calm around it.", "Marcus Aurelius", "Meditations 4.49"}
    };

    private static final String[][] OCT = {
            {"The best revenge is to be unlike him who performed the injury.", "Marcus Aurelius", "Meditations 6.6"},
            {"Associate with people who are likely to improve you.", "Seneca", "Letters 7"},
            {"A good character is the only guarantee of everlasting, carefree happiness.", "Seneca", "Letters 27 (adapted)"},
            {"Just as the good of a vine is grapes, the good of a man is good acts.", "Marcus Aurelius", "Meditations 5.6 (adapted)"},
            {"No man is good by accident. Virtue must be learned.", "Seneca", "Letters 123"},
            {"If you always remember that in all you do, in soul or body, God stands by as a witness, you will not err in your prayers or acts.", "Epictetus", "Discourses 2.8 (adapted)"},
            {"Be tolerant with others and strict with yourself.", "Marcus Aurelius", "Meditations 5.33 (adapted)"},
            {"He who does wrong wrongs himself; he who acts unjustly acts unjustly to himself, because he makes himself bad.", "Marcus Aurelius", "Meditations 9.4"},
            {"The greatest portion of goodness is the will to become good.", "Seneca", "Letters 34"},
            {"Nothing is more honourable than a grateful heart.", "Seneca", "Letters 81"},
            {"Kindness is invincible, if it be genuine and not a mask.", "Marcus Aurelius", "Meditations 11.18"},
            {"Do good to others quietly, as the vine bears grapes and asks nothing once it has borne its proper fruit.", "Marcus Aurelius", "Meditations 5.6"},
            {"Virtue is nothing else than right reason.", "Seneca", "Letters 66"},
            {"First tell yourself what kind of person you want to be, then do what you have to do.", "Epictetus", "Discourses 3.23 (adapted)"},
            {"Wherever there is a human being, there is a chance for kindness.", "Seneca", "On the Happy Life 24 (adapted)"},
            {"It is quality rather than quantity that matters.", "Seneca", "Letters 45"},
            {"Whatever anyone does or says, I must be good: as the emerald says, whatever anyone does or says, I must be emerald and keep my colour.", "Marcus Aurelius", "Meditations 7.15"},
            {"You should live for the other person if you wish to live for yourself.", "Seneca", "Letters 48"},
            {"Seek not the good in external things; seek it in yourselves.", "Epictetus", "Discourses 3.24 (adapted)"},
            {"When you do good and another benefits, why do you look for a third thing besides: reputation for the deed, or a return?", "Marcus Aurelius", "Meditations 7.73"},
            {"There is no man who is not made better by a friend's goodness.", "Seneca", "Letters 109 (adapted)"},
            {"The wrongdoer must be forgiven much: he errs unwillingly, in ignorance of the good.", "Epictetus", "Discourses 1.18 (adapted)"},
            {"Injustice is impiety; nature made rational creatures for the sake of one another.", "Marcus Aurelius", "Meditations 9.1"},
            {"To be good, a man must both understand the good and practise it.", "Musonius Rufus", "Lectures (paraphrase)"},
            {"A benefit consists not in what is done or given, but in the intention of the giver or doer.", "Seneca", "On Benefits 1.6"},
            {"If a man is mistaken, instruct him kindly and show him his error. If you cannot, blame yourself, or blame not even yourself.", "Marcus Aurelius", "Meditations 10.4"},
            {"Character is the proper good of man; guard it above fortune.", "Seneca", "Letters 41 (adapted)"},
            {"Consider what men are when they eat, sleep, boast; then what they are when they aim at virtue: how rare and worthy a sight.", "Epictetus", "Discourses (paraphrase)"},
            {"Straight, not straightened: be upright by your own nature, not held up by others.", "Marcus Aurelius", "Meditations 3.5 (adapted)"},
            {"Goodness that is concealed still shines; virtue needs no audience.", "Seneca", "Letters 79 (paraphrase)"},
            {"The fruit of this life is a good character and acts for the common good.", "Marcus Aurelius", "Meditations 6.30 (adapted)"}
    };

    private static final String[][] NOV = {
            {"Do not demand that events happen as you wish. Wish them to happen as they do happen, and your life will be serene.", "Epictetus", "Enchiridion 8"},
            {"Loss is nothing else but change, and change is nature's delight.", "Marcus Aurelius", "Meditations 9.35"},
            {"He is a wise man who does not grieve for the things which he has not, but rejoices for those which he has.", "Epictetus", "Fragments"},
            {"Fate leads the willing and drags along the reluctant.", "Seneca", "Letters 107 (quoting Cleanthes)"},
            {"Accept the things to which fate binds you, and love the people with whom fate brings you together, and do so with all your heart.", "Marcus Aurelius", "Meditations 6.39 (adapted)"},
            {"Whatever happens to you was prepared for you from eternity, and the thread of causes was spinning it from of old.", "Marcus Aurelius", "Meditations 10.5"},
            {"Seek not that the things which happen should happen as you wish; but wish the things which happen to be as they are.", "Epictetus", "Enchiridion 8"},
            {"It is madness to be dragged rather than to follow.", "Seneca", "Letters 107 (adapted)"},
            {"All that is in tune with you, O Universe, is in tune with me.", "Marcus Aurelius", "Meditations 4.23"},
            {"Everything harmonises with me which harmonises with you, O World. Nothing for me is too early or too late which is in due time for you.", "Marcus Aurelius", "Meditations 4.23"},
            {"Do not wish for things to be easy; wish yourself to be equal to them.", "Epictetus", "Discourses (paraphrase)"},
            {"A wise man never complains of what is, for he knows that complaint changes nothing but himself, and for the worse.", "Seneca", "Letters (paraphrase)"},
            {"What upsets us is wanting things the world has not agreed to give.", "Epictetus", "Enchiridion 2 (paraphrase)"},
            {"Willingly give yourself up to Clotho, allowing her to spin your thread into whatever web she pleases.", "Marcus Aurelius", "Meditations 4.34"},
            {"Nothing will ever befall me that I will receive with gloom or a bad disposition.", "Seneca", "On Providence 2 (adapted)"},
            {"Remember to conduct yourself in life as at a banquet: take politely what is passed to you; do not reach for what has not yet come.", "Epictetus", "Enchiridion 15 (adapted)"},
            {"Whatever may happen to you, it was prepared for you in advance from the beginning of time.", "Marcus Aurelius", "Meditations 10.5 (adapted)"},
            {"To grieve over what you cannot change is to add a second wound to the first.", "Seneca", "Letters (paraphrase)"},
            {"Never say of anything, 'I lost it,' but rather, 'I gave it back.' Your child died? It was given back. Your estate was taken? Was that not also given back?", "Epictetus", "Enchiridion 11"},
            {"The universe is change; our life is what our thoughts make it.", "Marcus Aurelius", "Meditations 4.3"},
            {"Consider that everything which happens, happens justly; if you observe carefully, you will find it so.", "Marcus Aurelius", "Meditations 4.10"},
            {"He who complies with necessity is wise, and skilled in things divine.", "Epictetus", "Fragments (quoting Euripides)"},
            {"What is the use of resisting? Nature wins; our part is assent.", "Seneca", "Letters (paraphrase)"},
            {"Sickness came? It came to me, not to my will. Loss came? It too stops at the door of the will.", "Epictetus", "Discourses (paraphrase)"},
            {"Receive prosperity without arrogance, and be ready to let it go with calm.", "Marcus Aurelius", "Meditations 8.33 (adapted)"},
            {"Love the discipline you know, and let it support you.", "Marcus Aurelius", "Meditations 4.31 (adapted)"},
            {"Nothing happens to the wise man against his expectation; he foresaw that anything could happen.", "Seneca", "On Tranquility of Mind 13 (adapted)"},
            {"Demand not that events should happen as you wish, and your life will flow well.", "Epictetus", "Enchiridion 8 (adapted)"},
            {"The willing soul fate leads; the unwilling it drags.", "Seneca", "Letters 107 (quoting Cleanthes)"},
            {"Pass through this little space of time in accordance with nature, and end your journey content, as an olive falls when ripe, blessing the tree that bore it.", "Marcus Aurelius", "Meditations 4.48"}
    };

    private static final String[][] DEC = {
            {"It is not that we have a short time to live, but that we waste much of it.", "Seneca", "On the Shortness of Life 1"},
            {"It is not death that a man should fear, but never beginning to live.", "Marcus Aurelius", "Meditations 12.1 (adapted)"},
            {"The whole future lies in uncertainty. Live immediately.", "Seneca", "On the Shortness of Life 9"},
            {"Do every act of your life as though it were the very last act of your life.", "Marcus Aurelius", "Meditations 2.5"},
            {"Think of yourself as dead. You have lived your life. Now take what is left and live it properly.", "Marcus Aurelius", "Meditations 7.56 (adapted)"},
            {"Let us prepare our minds as if we had come to the very end of life. Let us postpone nothing.", "Seneca", "Letters 101"},
            {"You could leave life right now. Let that determine what you do and say and think.", "Marcus Aurelius", "Meditations 2.11 (adapted)"},
            {"Keep death and exile before your eyes each day, and you will never entertain an abject thought.", "Epictetus", "Enchiridion 21 (adapted)"},
            {"He who has learned to die has unlearned slavery.", "Seneca", "Letters 26 (adapted)"},
            {"Death smiles at us all; all a man can do is smile back.", "Marcus Aurelius", "Meditations (attr.)"},
            {"Begin at once to live, and count each separate day as a separate life.", "Seneca", "Letters 101"},
            {"It is one thing to remember, another to know. Remembering is safeguarding something entrusted to the memory; knowing means making it your own.", "Seneca", "Letters 33"},
            {"Perfection of character is this: to live each day as if it were your last, without frenzy, without apathy, without pretence.", "Marcus Aurelius", "Meditations 7.69"},
            {"No man can have a peaceful life who thinks too much about lengthening it.", "Seneca", "Letters 4"},
            {"Think of the life you have lived until now as over, and, as a dead man, see what is left as a bonus, and live it according to nature.", "Marcus Aurelius", "Meditations 7.56"},
            {"He will live badly who does not know how to die well.", "Seneca", "On Tranquility of Mind 11"},
            {"Do not act as if you were going to live ten thousand years. Death hangs over you. While you live, while it is in your power, be good.", "Marcus Aurelius", "Meditations 4.17"},
            {"What is death? A mask that frightens children. Turn it round and examine it: see, it does not bite.", "Epictetus", "Discourses 2.1 (adapted)"},
            {"Life, if well lived, is long enough.", "Seneca", "On the Shortness of Life 2 (adapted)"},
            {"Everything is only for a day, both that which remembers and that which is remembered.", "Marcus Aurelius", "Meditations 4.35"},
            {"You are afraid of dying; but come now, how is this life of yours anything but dying?", "Seneca", "Letters 77 (adapted)"},
            {"I cannot escape death; shall I not escape the fear of it?", "Epictetus", "Discourses 1.27"},
            {"Soon you will have forgotten all things; soon all things will have forgotten you.", "Marcus Aurelius", "Meditations 7.21"},
            {"Every day, therefore, should be regulated as if it were the one that brings up the rear, the one that rounds out and completes our lives.", "Seneca", "Letters 12"},
            {"Time is a river of passing events, and strong is its current; no sooner is a thing brought to sight than it is swept by and another takes its place.", "Marcus Aurelius", "Meditations 4.43"},
            {"He who dreads death dreads the loss of sensation or a different sensation. But either you will have no sensation, or a different kind of life.", "Marcus Aurelius", "Meditations 8.58 (adapted)"},
            {"Let us so order our minds as if we had come to the very end. Balance life's books each day.", "Seneca", "Letters 101 (adapted)"},
            {"Depart then satisfied, for he also who releases you is satisfied.", "Marcus Aurelius", "Meditations 12.36"},
            {"While we postpone, life speeds by. Nothing is ours except time.", "Seneca", "Letters 1"},
            {"Live each day as your last, and each morning as a gift you did not expect.", "Seneca", "Letters 12 (paraphrase)"},
            {"Pass then through this little space of time conformably to nature, and end your journey in content.", "Marcus Aurelius", "Meditations 4.48"}
    };

    private static final String[][][] YEAR = {
            JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    };

    private Quotes() {
    }

    /** Quote for a calendar month (0-based) and day of month (1-based). */
    static String[] forDate(int month, int day) {
        String[][] monthQuotes = YEAR[month];
        int index = Math.min(Math.max(day - 1, 0), monthQuotes.length - 1);
        return monthQuotes[index];
    }

    static String[] today() {
        Calendar c = Calendar.getInstance();
        return forDate(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    /** Theme label for a calendar month (0-based). */
    static String theme(int month) {
        return THEMES[month];
    }

    /** Journal prompt for a date: rotates through the month's five prompts. */
    static String prompt(int month, int day) {
        String[] pool = PROMPTS[month];
        return pool[(day - 1) % pool.length];
    }

    static String todayPrompt() {
        Calendar c = Calendar.getInstance();
        return prompt(c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    static String todayTheme() {
        return theme(Calendar.getInstance().get(Calendar.MONTH));
    }
}
