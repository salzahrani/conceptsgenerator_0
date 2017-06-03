package triplet.combined;

import triplet.alchamy.Main;

import java.io.IOException;

/**
 * Created by sultanalzahrani on 5/28/17.
 */
public class AllExtractors
{
    public static void main(String[] args) throws Exception {


        tools.Ticktock t = new tools.Ticktock();

        // Everest:
        RunnableEverest T0 = new RunnableEverest( "Everest");
        T0.start();


        // Reverb
        ThreadReverb T1 = new ThreadReverb( "Reverb");
        T1.start();


        //ClearNLP
        ThreadClearNLP T2 = new ThreadClearNLP( "ClearNLP");
        T2.start();

        //alchamy
        ThreadAlchamy T3 = new ThreadAlchamy("Alchamy");
        T3.start();





        String str = t.betterFormat();
        System.out.printf("Total Time of all parsers>>> "+ str);





    }

    static class RunnableEverest extends Thread {
        private Thread t;
        private String threadName;

        RunnableEverest( String name) {
            threadName = name;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            tools.Ticktock t = new tools.Ticktock();
            triplet.everest.Main everst =  new triplet.everest.Main();
            try {
                triplet.everest.Main.parseIt();

            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = t.betterFormat();
            System.out.printf(threadName + " >>> "+ str);

        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }


    static class ThreadReverb extends Thread {
        private Thread t;
        private String threadName;

        ThreadReverb( String name) {
            threadName = name;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            tools.Ticktock t = new tools.Ticktock();
            triplet.reverb.Main reverb =  new triplet.reverb.Main();
            try {
                triplet.reverb.Main.parseIt();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = t.betterFormat();
            System.out.printf(threadName + " >>> "+ str);
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }

    static class ThreadClearNLP extends Thread {
        private Thread t;
        private String threadName;

        ThreadClearNLP( String name) {
            threadName = name;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            tools.Ticktock t = new tools.Ticktock();
            triplet.clearNLP.Main reverb =  new triplet.clearNLP.Main();
            try {
                triplet.clearNLP.Main.parseIt();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = t.betterFormat();
            System.out.printf(threadName + " >>> "+ str);
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }

    static class ThreadAlchamy extends Thread {
        private Thread t;
        private String threadName;

        ThreadAlchamy( String name) {
            threadName = name;
            System.out.println("Creating " +  threadName );
        }

        public void run() {
            tools.Ticktock t = new tools.Ticktock();
            triplet.alchamy.Main alchamy = new triplet.alchamy.Main();
            try {
                Main.parseIt();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = t.betterFormat();
            System.out.printf(threadName + " >>> "+ str);
        }

        public void start () {
            System.out.println("Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            }
        }
    }



}
