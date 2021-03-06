package Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Parser.generatednodes.ASTNode;
import Parser.generatednodes.ASTGenerated_class_declaration;
import Parser.generatednodes.ASTGenerated_class_instantiation;
import Parser.generatednodes.ASTGenerated_class_method;
import Parser.generatednodes.ASTGenerated_class_property_get;
import Parser.generatednodes.ASTGenerated_data_type;
import Parser.generatednodes.ASTGenerated_data_type_boolean;
import Parser.generatednodes.ASTGenerated_data_type_decimal;
import Parser.generatednodes.ASTGenerated_data_type_integer;
import Parser.generatednodes.ASTGenerated_data_type_string;
import Parser.generatednodes.ASTGenerated_expression_no_parenthesis;
import Parser.generatednodes.ASTGenerated_expression_parenthesis;
import Parser.generatednodes.ASTGenerated_func_action;
import Parser.generatednodes.ASTGenerated_func_call;
import Parser.generatednodes.ASTGenerated_func_declaration;
import Parser.generatednodes.ASTGenerated_identifier;
import Parser.generatednodes.ASTGenerated_operator_addition;
import Parser.generatednodes.ASTGenerated_operator_and;
import Parser.generatednodes.ASTGenerated_operator_division;
import Parser.generatednodes.ASTGenerated_operator_equal_to;
import Parser.generatednodes.ASTGenerated_operator_greater_than;
import Parser.generatednodes.ASTGenerated_operator_greater_than_equal_to;
import Parser.generatednodes.ASTGenerated_operator_less_than;
import Parser.generatednodes.ASTGenerated_operator_less_than_equal_to;
import Parser.generatednodes.ASTGenerated_operator_modulous;
import Parser.generatednodes.ASTGenerated_operator_multiplication;
import Parser.generatednodes.ASTGenerated_operator_not;
import Parser.generatednodes.ASTGenerated_operator_not_equal_to;
import Parser.generatednodes.ASTGenerated_operator_or;
import Parser.generatednodes.ASTGenerated_operator_subtraction;
import Parser.generatednodes.ASTGenerated_operators;
import Parser.generatednodes.ASTGenerated_statement_emit;
import Parser.generatednodes.ASTGenerated_statement_if;
import Parser.generatednodes.ASTGenerated_statement_pass;
import Parser.generatednodes.ASTGenerated_statement_while;
import Parser.generatednodes.ASTGenerated_statements;
import Parser.generatednodes.ASTGenerated_val_no_expression;
import Parser.generatednodes.ASTGenerated_val_no_expression_no_parenthesis;
import Parser.generatednodes.ASTGenerated_value;
import Parser.generatednodes.ASTGenerated_variable_assignment;
import Parser.generatednodes.ASTGenerated_variable_declaration;
import Parser.generatednodes.ASTNode;

public class RuntimeNode {
  public static void runRootNode(ASTNode node) {
    RuntimeConstants.setGlobalConstants();
    RuntimeNode.runASTNodes(node.getChildren(), new RuntimeContext(null), false);
  }
  
  public static void runASTNode(ASTNode n, RuntimeContext context, boolean insideFunc) {
    if(n instanceof ASTGenerated_func_action) {
      runASTNode(n.getChild(0), context, insideFunc);
      return;
    }
    
    if(n instanceof ASTGenerated_func_call) {
      runASTNode(n.getChild(0), context, insideFunc);
      return;
    }
    
    if(n instanceof ASTGenerated_variable_declaration) {
      String type = n.getChild(0).getChild(0).getNodeValue();
      String varName = n.getChild(1).getChild(0).getNodeValue();
      ObjectRepresentation assessedValue = assessASTNode(n.getChild(2), context);
      
      if(RuntimeContext.getClass(type) != assessedValue.getObjectClassRepresentation()) {
        throw new RuntimeNodeException(
            "Type: `" + type + "` does not match given type."
        );
      }
      
      context.setLocal(varName, assessedValue);
    }
    
    if(n instanceof ASTGenerated_variable_assignment) {
      String varName = n.getChild(0).getChild(0).getNodeValue();
      ObjectRepresentation assessedValue = assessASTNode(n.getChild(1), context);
      
      context.updateObject(varName, assessedValue);
    }
    
    if(n instanceof ASTGenerated_func_call) {
      String funcName = n.getChild(0).getChild(0).getNodeValue();
      ASTNode[] vals = n.getChild(1).getChildren();
      List<ObjectRepresentation> args = new LinkedList<ObjectRepresentation>();
      for(int i = 0; i < vals.length; i++) {
        args.add(assessASTNode(vals[i], context));
      }
      
      FunctionRepresentation funcRep = context.getFunction(funcName);
      funcRep.call(context.getOpenObject(), args.toArray(new ObjectRepresentation[0]));
    }
    
    if(n instanceof ASTGenerated_class_method) {
      String objName = n.getChild(0).getChild(0).getNodeValue();
      String funcName = n.getChild(1).getChild(0).getChild(1).getNodeValue();
      ASTNode[] vals = n.getChild(1).getChild(1).getChildren();
      List<ObjectRepresentation> args = new LinkedList<ObjectRepresentation>();
      
      for(int i = 0; i < vals.length; i++) {
        args.add(assessASTNode(vals[i], context));
      }
      
      context.getObject(objName).runMethod(funcName, args.toArray(new ObjectRepresentation[0]));
    }
    
    if(n instanceof ASTGenerated_statement_emit) {
      ObjectRepresentation assessedValue = assessASTNode(n.getChild(0), context);
      if(assessedValue.getObjectClassRepresentation() == RuntimeContext.getClass("String")) {
        System.out.println(assessedValue.getBaseVal());
        return;
      }
      
      System.out.println(
          assessedValue.runMethod("toString", new ObjectRepresentation[] {}).getBaseVal()
      );
      return;
    }
    
    if(n instanceof ASTGenerated_statement_pass) {
      System.out.println("returned: " + assessASTNode(n.getChild(0), context));
      System.exit(0);
    }
    
    if(n instanceof ASTGenerated_statement_while) {
      ObjectRepresentation assessedValue = assessASTNode(n.getChild(0), context);
      if(assessedValue.getObjectClassRepresentation() != RuntimeConstants.getBooleanClass()) {
        throw new RuntimeNodeException("Expected `Boolean` type, but was not found.");
      }
      
      while ((Boolean) assessedValue.getBaseVal() == true) {
        runASTNodes(n.getChild(1).getChildren(), context, insideFunc);
      }
    }
    
    
    if(n instanceof ASTGenerated_statement_if) {
      ObjectRepresentation assessedValue = assessASTNode(n.getChild(0), context);
      if(assessedValue.getObjectClassRepresentation() != RuntimeConstants.getBooleanClass()) {
        throw new RuntimeNodeException("Expected `Boolean` type, but was not found.");
      }
      
      if((boolean) assessedValue.getBaseVal() == true) {
        runASTNodes(n.getChild(1).getChildren(), context, insideFunc);
      }
    }
    
    
    if(n instanceof ASTGenerated_class_declaration) {
      ASTNode[] children = n.getChildren();
      String className = children[0].getNodeValue();
      String extendsClassName = children[1].getNodeValue();
      ClassRepresentation extendsClass;
      
      if(extendsClassName == null || extendsClassName == "") {
        extendsClass = RuntimeConstants.getObjectClass();
      }else {
        extendsClass = RuntimeContext.getClass(extendsClassName);
      }
      
      DefaultPropsContainer extendsOverrides = new DefaultPropsContainer();
      DefaultPropsContainer defProps = new DefaultPropsContainer();
      MethodContainer<Object> methods = new MethodContainer<Object>();
      ASTNode[] overrideIdentifiers = children[2].getChildren();
      ASTNode[] propNodes = children[3].getChildren();
      ASTNode[] functionNodes = children[4].getChildren();
      
      for(int i = 0; i < overrideIdentifiers.length; i++) {
        String propName = overrideIdentifiers[i].getChild(0).getChild(0).getNodeValue();
        ObjectRepresentation assessedValue = assessASTNode(overrideIdentifiers[i].getChild(1), context);
        extendsOverrides.put(propName, assessedValue);
      }
      
      for(int i = 0; i < propNodes.length; i++) {
        String typeIdentifier = propNodes[i].getChild(0).getChild(0).getNodeValue();
        String propIdentifier = propNodes[i].getChild(1).getChild(0).getNodeValue();
        ObjectRepresentation assessedValue = assessASTNode(propNodes[i].getChild(2), context); 
        
        if(RuntimeContext.getClass(className) != assessedValue.getObjectClassRepresentation()) {
          throw new RuntimeNodeException(
              "Type `" + typeIdentifier + "` does not match found type."
          );
        }
        defProps.put(propIdentifier, assessedValue);
      }
      
      for(int i = 0; i < functionNodes.length; i++) {
        String funcName = functionNodes[i].getChild(0).getChild(0).getNodeValue();
        ASTNode[] paramNodes = functionNodes[i].getChild(1).getChildren();
        String returnTypeIdentifier = functionNodes[i].getChild(2).getChild(0).getNodeValue();
        ASTNode body = functionNodes[i].getChild(3);
        ParameterContainer params = new ParameterContainer();
        
        for(int j = 0; j < paramNodes.length; j++) {
          String typeName = paramNodes[j].getChild(0).getChild(0).getNodeValue();
          String paramName = paramNodes[j].getChild(1).getChild(0).getNodeValue();
          params.put(paramName, RuntimeContext.getClass(typeName));
        }
        
        methods.put(funcName, new FunctionRepresentation<Object>(params, body, RuntimeContext.getClass(returnTypeIdentifier), funcName));
      }
      
      RuntimeContext.setClass(className, new ClassRepresentation<Object>(extendsOverrides, defProps, methods, className, extendsClass));
      
    }
    
    if(n instanceof ASTGenerated_func_declaration) {
      String funcName = n.getChild(0).getChild(0).getNodeValue();
      ASTNode[] params = n.getChild(1).getChildren();
      String returnTypeIdentifier = n.getChild(2).getChild(0).getNodeValue();
      ASTNode body = n.getChild(3);
      ParameterContainer parameters = new ParameterContainer();
      
      for(int j = 0; j < params.length; j++) {
        String typeIdentifier = params[j].getChild(0).getChild(0).getNodeValue();
        String paramIdentifier = params[j].getChild(1).getChild(0).getNodeValue();
        parameters.put(paramIdentifier, RuntimeContext.getClass(typeIdentifier));
      }
      
      RuntimeContext.setGlobalFunction(funcName, 
          new FunctionRepresentation<Object>(
              parameters, 
              body, 
              RuntimeContext.getClass(returnTypeIdentifier), 
              funcName
        )
      );
    }
  }
  
  public static ObjectRepresentation runASTNodes(ASTNode[] nodes, RuntimeContext context, boolean insideFunc) {
    for(int i = 0; i < nodes.length; i++) {
      if(insideFunc) {
        ASTNode n = nodes[i];
        
        if(n instanceof ASTGenerated_func_action) {
          n = n.getChild(0);
        }
        
        if(n instanceof ASTGenerated_statements) {
          n = n.getChild(0);
        }
        
        if(n instanceof ASTGenerated_statement_pass) {
          return assessASTNode(n.getChildren()[0], context);
        }
        
      }
      
      runASTNode(nodes[i], context, insideFunc);
    }
    
    if(!insideFunc) {
      return null;
    }else {
      return RuntimeContext.getGlobalObject("undefined");
    }
  }
  
  
  public static ObjectRepresentation assessASTNode(ASTNode node, RuntimeContext context) {
    if(node instanceof ASTGenerated_value || node instanceof ASTGenerated_val_no_expression || node instanceof ASTGenerated_val_no_expression_no_parenthesis) {
      if(node.getChild(0) instanceof ASTGenerated_operator_not) {
        ObjectRepresentation assessedChildObj = assessASTNode(node.getChild(1), context);
        
        if(assessedChildObj.getObjectClassRepresentation() != RuntimeConstants.getBooleanClass()) {
          throw new RuntimeNodeException("Expected `Boolean` type, but was not found.");
        }
        
        return assessedChildObj.runMethod(
            "equals",
            new ObjectRepresentation[] {
                RuntimeConstants.getBooleanClass().createObject(false)
            }
        );
      }
      return assessASTNode(node.getChildren()[0], context);
    }
    
    if(node instanceof ASTGenerated_data_type) {
      return assessASTNode(node.getChildren()[0], context);
    }
    
    if(node instanceof ASTGenerated_data_type_boolean) {
      if(node.getNodeValue() == "true") {
        return RuntimeConstants.getBooleanClass().createObject(true);
      }else if(node.getNodeValue() == "false"){
        return RuntimeConstants.getBooleanClass().createObject(false);
      }else {
        throw new RuntimeNodeException("The values true or false were expected, but got: " + node.getNodeValue());
      }
    }
    
    if(node instanceof ASTGenerated_data_type_string) {
      if(!node.getNodeValue().startsWith("\"")) {
        throw new RuntimeNodeException(
            "Expected string to begin with `\"`, but none was found."
        );
      }
      
      if(!node.getNodeValue().endsWith("\"")) {
        throw new RuntimeNodeException(
            "Expected string to end with `\"`, but none was found."
        );
      }
      
      return RuntimeConstants.getStringClass().createObject(node.getNodeValue().substring(1, node.getNodeValue().length() - 1));
    }
    
    if(node instanceof ASTGenerated_data_type_integer) {
      return RuntimeConstants.getIntegerClass().createObject(Long.parseLong(node.getNodeValue()));
    }
    
    if(node instanceof ASTGenerated_identifier) {
      return context.getObject(node.getChild(0).getNodeValue());
    }
    
    if(node instanceof ASTGenerated_data_type_decimal) {
      return RuntimeConstants.getDoubleClass().createObject(Double.parseDouble(node.getNodeValue()));
    }
    
    if(node instanceof ASTGenerated_func_call) {
      String funcName = node.getChild(0).getChild(0).getNodeValue();
      ASTNode[] vals = node.getChild(1).getChildren();
      List<ObjectRepresentation> args = new LinkedList<ObjectRepresentation>();
      
      for(int i = 0; i < vals.length; i++) {
        args.add(assessASTNode(vals[i], context));
      }
      
      FunctionRepresentation funcRep = context.getFunction(funcName);
      return funcRep.call(
          context.getOpenObject(),
          args.toArray(new ObjectRepresentation[0])
      );
    }
    
    if(node instanceof ASTGenerated_class_instantiation) {
      String classIdentifier = node.getChild(0).getChild(0).getNodeValue();
      ASTNode[] vals = node.getChild(1).getChildren();
      List<ObjectRepresentation> args = new LinkedList<ObjectRepresentation>();
      
      for(int i = 0; i < vals.length; i++) {
        args.add(assessASTNode(vals[i], context));
      }
      
      ClassRepresentation createdClass = RuntimeContext.getClass(classIdentifier);
      return createdClass.createObject(
          args.toArray(new ObjectRepresentation[0]),
          null
      );
    }
    
    if(node instanceof ASTGenerated_class_method) {
      String objIdentifier = node.getChild(0).getChild(0).getNodeValue();
      String methodName = node.getChild(1).getChild(0).getNodeValue();
      ASTNode[] vals = node.getChild(1).getChild(1).getChildren();
      List<ObjectRepresentation> args = new LinkedList<ObjectRepresentation>();
      
      for(int i = 0; i < vals.length; i++) {
        args.add(assessASTNode(vals[i], context));
      }
      
      return context.getObject(objIdentifier).runMethod(methodName, args.toArray(new ObjectRepresentation[0]));
    }
    
    if(node instanceof ASTGenerated_class_property_get) {
      String objIdentifier = node.getChild(0).getChild(0).getNodeValue();
      String propIdentifier = node.getChild(1).getNodeValue();
      
      return context.getObject(objIdentifier).getProp(propIdentifier);
    }
    
    if(node instanceof ASTGenerated_expression_parenthesis
        || node instanceof ASTGenerated_expression_no_parenthesis) {
      ASTNode[] expressChildren = node.getChildren();
      List<Object> expressChildrenRemaining = new ArrayList<Object>(Arrays.asList(expressChildren));
      int expressSteps = (expressChildren.length - 1) / 2;
      ObjectRepresentation curObj = null;
      
      while(1 < expressChildrenRemaining.size()) {
        int highestPrecedence = -1;
        int curHighestPrecedenceIndex = -1;
        
        for(int i = 0; i < expressChildrenRemaining.size(); i+=2) {
          int precedence = precedenceForBinaryOperator(
              (ASTNode) expressChildrenRemaining.get(i)
          );

          if (highestPrecedence < precedence) {
            highestPrecedence = precedence;
              curHighestPrecedenceIndex = i;
          }
        }
        
        ObjectRepresentation newObj = evaluateObjectExpressionStep(
            expressChildrenRemaining.get(curHighestPrecedenceIndex - 1),
            (ASTNode) expressChildrenRemaining.get(curHighestPrecedenceIndex),
            expressChildrenRemaining.get(curHighestPrecedenceIndex + 1),
            context
        );
        
        expressChildrenRemaining.remove(curHighestPrecedenceIndex - 1);
        expressChildrenRemaining.remove(curHighestPrecedenceIndex - 1);
        expressChildrenRemaining.remove(curHighestPrecedenceIndex - 1);
        expressChildrenRemaining.add(curHighestPrecedenceIndex - 1, newObj);
      }
      return (ObjectRepresentation) expressChildrenRemaining.get(0);
    }
    return RuntimeContext.getGlobalObject("undefined");
  }
  
  private static ObjectRepresentation evaluateObjectExpressionStep(
      Object term,
      ASTNode operatorNode,
      Object term2,
      RuntimeContext context) {
    ObjectRepresentation termObject;
    ObjectRepresentation term2Object;
    
    if (term instanceof ASTNode) {
      termObject = assessASTNode((ASTNode) term, context);
    } else if (term instanceof ObjectRepresentation) {
      termObject = (ObjectRepresentation) term;
    } else {
        throw new RuntimeNodeException(
            "Term 0 was neither a node nor a created object."
        );
    }
  
    if (term2 instanceof ASTNode) {
        term2Object = assessASTNode((ASTNode) term2, context);
    } else if (term2 instanceof ObjectRepresentation) {
      term2Object = (ObjectRepresentation) term2;
    } else {
        throw new RuntimeNodeException(
            "Term 1 was neither a node nor a created object."
        );
    }
    
    return evaluateExpressionStep(
        termObject,
        operatorNode,
        term2Object,
        context
    );
  }
  
  public static ObjectRepresentation evaluateExpressionStep(ObjectRepresentation term,
      ASTNode operatorNode,
      Object term2,
      RuntimeContext context) {
    return term.runMethod(methodNameForBinaryOperator(operatorNode),
        new ObjectRepresentation[] { term });
  }
  
  private static String methodNameForBinaryOperator(ASTNode operatorNode) {
    if (operatorNode instanceof ASTGenerated_operators) {
        operatorNode = operatorNode.getChild(0);
    }

    if (operatorNode instanceof ASTGenerated_operator_equal_to) {
        return "equals";
    }

    if (operatorNode instanceof ASTGenerated_operator_not_equal_to) {
        return "notEqual";
    }

    if (operatorNode instanceof ASTGenerated_operator_addition) {
        return "addition";
    }

    if (operatorNode instanceof ASTGenerated_operator_subtraction) {
        return "subtraction";
    }

    if (operatorNode instanceof ASTGenerated_operator_multiplication) {
        return "multiplication";
    }

    if (operatorNode instanceof ASTGenerated_operator_division) {
        return "division";
    }

    if (operatorNode instanceof ASTGenerated_operator_modulous) {
        return "modulo";
    }


    if (operatorNode instanceof ASTGenerated_operator_and) {
        return "andStatement";
    }

    if (operatorNode instanceof ASTGenerated_operator_or) {
        return "orStatement";
    }

    if (operatorNode instanceof ASTGenerated_operator_greater_than) {
        return "greaterThan";
    }

    if (operatorNode instanceof ASTGenerated_operator_less_than) {
        return "lessThan";
    }

    if (operatorNode instanceof ASTGenerated_operator_greater_than_equal_to) {
        return "greaterThanEqual";
    }

    if (operatorNode instanceof ASTGenerated_operator_less_than_equal_to) {
        return "lessThanEqual";
    }

    throw new RuntimeNodeException("Binary operator found is unsupported.");
}

private static int precedenceForBinaryOperator(ASTNode operatorNode) {
    if (operatorNode instanceof ASTGenerated_operators) {
        operatorNode = operatorNode.getChild(0);
    }

    if (operatorNode instanceof ASTGenerated_operator_equal_to) {
        return 30;
    }

    if (operatorNode instanceof ASTGenerated_operator_not_equal_to) {
        return 30;
    }

    if (operatorNode instanceof ASTGenerated_operator_addition) {
        return 40;
    }

    if (operatorNode instanceof ASTGenerated_operator_subtraction) {
        return 40;
    }

    if (operatorNode instanceof ASTGenerated_operator_multiplication) {
        return 50;
    }

    if (operatorNode instanceof ASTGenerated_operator_division) {
        return 50;
    }

    if (operatorNode instanceof ASTGenerated_operator_modulous) {
        return 50;
    }


    if (operatorNode instanceof ASTGenerated_operator_and) {
        return 20;
    }

    if (operatorNode instanceof ASTGenerated_operator_or) {
        return 10;
    }

    if (operatorNode instanceof ASTGenerated_operator_greater_than) {
        return 30;
    }

    if (operatorNode instanceof ASTGenerated_operator_less_than) {
        return 30;
    }

    if (operatorNode instanceof ASTGenerated_operator_greater_than_equal_to) {
        return 30;
    }

    if (operatorNode instanceof ASTGenerated_operator_less_than_equal_to) {
        return 30;
    }

    throw new RuntimeNodeException("Binary operator found is unsupported.");
}
  
}

class RuntimeNodeException extends RuntimeException {
  public RuntimeNodeException(String message) {
      super(message);
  }
}
