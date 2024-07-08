//import React, { useReducer } from "react";
//
//        const Tasks = [
//        {
//        "taskId": 1,
//        "title": "Grocery Shopping",
//        "description": "Buy groceries from the supermarket",
//        "status": "open",
//        "assignedTo": ['Mary', 'Peter'],
//        "createdBy": 201,
//        "createdAt": "2024-06-12T10:00:00Z"
//        },
//        {
//        "taskId": 2,
//        "title": "Lawn Mowing",
//        "description": "Mow the lawn at the front yard",
//        "status": "completed",
//        "assignedTo": ['David', 'Peter'],
//        "createdBy": 202,
//        "createdAt": "2024-06-10T08:00:00Z",
//        "completedAt": "2024-06-11T15:00:00Z"
//        }
//        ];
//
//        const initialState = [...Tasks];
//
//        const reducer = (state, action) => {
//        switch (action.type) {
//        case "ADD_TASK":
//        return [...state, action.payload];
//        case "DELETE_TASK":
//        return state.filter(task => task.taskId !== action.payload);
//        case "UPDATE_TASK":
//                return state.map(task =>
//        task.taskId === action.payload.taskId
//        ? { ...task, ...action.payload.updates }
//                    : task
//            );
//default:
//        return state;
//    }
//            };
//
//            const TaskList = () => {
//        const [tasks, dispatch] = useReducer(reducer, initialState);
//
//    const newTask = {
//taskId: 3,
//title: "Cleaning",
//description: "Clean the house",
//status: "open",
//assignedTo: ['John'],
//createdBy: 203,
//createdAt: "2024-06-13T14:00:00Z"
//        };
//
//        const handleAddTask = () => {
//dispatch({ type: "ADD_TASK", payload: newTask });
//        };
//
//        const handleDeleteTask = (taskId) => {
//dispatch({ type: "DELETE_TASK", payload: taskId });
//        };
//
//        const handleUpdateTask = (taskId, updates) => {
//dispatch({ type: "UPDATE_TASK", payload: { taskId, updates } });
//        };
//
//        return (
//        <>
//<h1>List All Tasks</h1>
//<ul>
//{tasks.map((task) => (
//        <li key={task.taskId}>
//                        <ul>{task.taskId}</ul>
//                        <ul>{task.title}</ul>
//                        <ul>{task.description}</ul>
//                        <ul>{task.status}</ul>
//                        <ul>{task.assignedTo.join(", ")}</ul>
//                        <ul>{task.createdBy}</ul>
//                        <ul>{task.createdAt}</ul>
//        {task.status === "completed" && <ul>{task.completedAt}</ul>}
//                    </li>
//                ))}
//            </ul>
//
//            <button onClick={handleAddTask}>Add Task</button>
//            <button onClick={() => handleDeleteTask(1)}>Delete Task 1</button>
//            <button onClick={() => handleUpdateTask(2, { title: "Updated Task" })}>Update Task 2</button>
//        </>
//        );
//        };
//
//export default TaskList;