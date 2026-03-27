package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateArgLen(args, 1);
                Repository.init();
                break;
            case "add":
                validateArgLen(args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validateArgLen(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                validateArgLen(args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                validateArgLen(args, 1);
                Repository.log();
                break;
            case "global-log":
                validateArgLen(args, 1);
                Repository.global_log();
                break;
            case "find":
                validateArgLen(args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                validateArgLen(args, 1);
                Repository.status();
                break;
            case "checkout":
                //checkout -- filename
                if(args.length == 3 && dashEqual(args[1])){
                    Repository.checkout(args[2]);
                }
                //checkout branch
                else if(args.length == 2){
                    Repository.checkoutBranch(args[1]);
                }
                //checkout [commit id] -- filename
                else if(args.length == 4 && dashEqual(args[2])){
                    Repository.checkout(args[1], args[3]);
                }
                else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            case "branch":
                validateArgLen(args, 2);
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                validateArgLen(args, 2);
                Repository.rm_branch(args[1]);
                break;
            case "reset":
                validateArgLen(args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                validateArgLen(args, 2);
                Repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    private static void validateArgLen(String[] arg, int goalLen){
        if(arg.length != goalLen){
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    private static boolean dashEqual(String arg){
        return arg.equals("--");
    }
}
