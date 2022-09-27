package rendutp1.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class NodeVisitor extends ASTVisitor {

        public boolean visit(ASTVisitor node) {       
            int lineNumber = getLineNumber(node.getStartPosition()) - 1;
            return true;
        }
    });
}
