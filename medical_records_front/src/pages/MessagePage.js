import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthProvider';

export default function MessagePage() {
    const { userId, userRole } = useAuth();
    const [messages, setMessages] = useState([]);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const response = await axios.get(`http://localhost:8082/${userId}/inbox`);
                setMessages(response.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        const fetchUserList = async () => {
            try {
                const userResponse = await axios.get('http://localhost:8080/users');
                setUsers(userResponse.data);
            } catch (error) {
                console.error('Error fetching user list:', error);
            }
        };

        fetchMessages();
        fetchUserList();
    }, [userId]);

    // Function to get sender's username by ID
    const getSenderUsername = (senderId) => {
        const sender = users.find(user => user.id === senderId);
        return sender ? sender.username : 'Unknown';
    };

    return (
        <div className="container mt-4">
            <div className="row">
                {/* Sidebar for Doctor/Staff List */}
                {userRole === 'PATIENT' && (
                    <div className="col-md-4">
                        <div className="card">
                            <div className="card-header bg-primary text-white">
                                <h5 className="mb-0">Doctors & Staff</h5>
                            </div>
                            <div className="card-body p-0">
                                <ul className="list-group list-group-flush">
                                    {users
                                        .filter(user => user.role !== 'PATIENT')
                                        .map(user => (
                                            <li key={user.id} className="list-group-item d-flex justify-content-between align-items-center">
                                                {user.username}
                                                <Link to={`/messages/new_message/${user.id}`} className="btn btn-sm btn-primary">
                                                    Message
                                                </Link>
                                            </li>
                                        ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                )}

                {/* Messages Inbox */}
                <div className={`col ${userRole === 'PATIENT' ? 'col-md-8' : 'col-md-12'}`}>
                    <div className="card">
                        <div className="card-header bg-success text-white">
                            <h5 className="mb-0">Inbox</h5>
                        </div>
                        <div className="card-body">
                            {loading ? (
                                <p>Loading messages...</p>
                            ) : messages.length > 0 ? (
                                <div className="list-group">
                                    {messages.map(message => (
                                        <div key={message.id} className="list-group-item">
                                            <div className="d-flex w-100 justify-content-between">
                                                <h6 className="mb-1">From: {getSenderUsername(message.sender)}</h6>
                                                {userRole !== 'PATIENT' && (
                                                    <Link to={`/messages/new_message/${message.sender}`} className="btn btn-sm btn-outline-primary">
                                                        Respond
                                                    </Link>
                                                )}
                                            </div>
                                            <p className="mb-1">{message.content}</p>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-muted">No messages found.</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
